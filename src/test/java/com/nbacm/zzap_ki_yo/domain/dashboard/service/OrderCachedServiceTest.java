package com.nbacm.zzap_ki_yo.domain.dashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.dashboard.dto.StatisticsResponseDto;
import com.nbacm.zzap_ki_yo.domain.dashboard.entity.Statistics;
import com.nbacm.zzap_ki_yo.domain.dashboard.repository.StatisticsRepository;  // 변경된 부분
import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderStatus;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import com.nbacm.zzap_ki_yo.domain.order.service.OrderServiceImpl;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.store.service.AdminStoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class OrderCachedServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private AdminStoreServiceImpl adminStoreService;

    @Mock
    private RedisTemplate<String, Object> objectRedisTemplate;  // RedisTemplate 모킹

    @Mock
    private ValueOperations<String, Object> valueOperations;   // ValueOperations 모킹

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderServiceImpl orderService;



    private Store store;
    private Statistics statistics;
    private StatisticsResponseDto statisticsResponseDto;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);

        // Store 객체 생성 (setter 없이 빌더 패턴 사용)
        store = Store.builder()

                .storeName("Test Store")
                .build();
        // Statistics 객체 생성
        statistics = Statistics.builder()
                .store(store)
                .date(LocalDate.now())
                .totalSales(1000)
                .customerCount(10)
                .build();
        setStoreId(store, 1L);
        // StatisticsResponseDto 객체 생성
        statisticsResponseDto = new StatisticsResponseDto(1500, 15);

        // RedisTemplate의 opsForValue를 모킹해서 valueOperations를 반환하도록 설정
        when(objectRedisTemplate.opsForValue()).thenReturn(valueOperations);

        // Mock RedisTemplate의 opsForValue().get()가 JSON 직렬화된 값을 반환하도록 설정
        String serializedDto = new ObjectMapper().writeValueAsString(statisticsResponseDto);
        when(objectRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(serializedDto);  // JSON 문자열 반환

        // Mock ObjectMapper 역직렬화
        when(objectMapper.readValue(anyString(), eq(StatisticsResponseDto.class)))
                .thenReturn(statisticsResponseDto);
    }
    // Reflection으로 storeId 설정
    private void setStoreId(Store store, Long id) {
        try {
            Field storeIdField = Store.class.getDeclaredField("storeId");
            storeIdField.setAccessible(true);  // private 필드에 접근 허용
            storeIdField.set(store, id);  // store 객체에 storeId 설정
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    void testUpdateStoreStatistics() {
        // Mock repository return for findByStoreIdAndDateBetween
        when(statisticsRepository.findByStoreIdAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(statistics));

        // 통계 업데이트 메소드 호출
        orderService.updateStoreStatistics(store, 500);

        // Repository와 Redis 상호작용 검증
        verify(statisticsRepository).save(any(Statistics.class));  // DB에 저장되는지 검증
        verify(objectRedisTemplate, times(2)).delete(anyString());  // 캐시 무효화 검증
        verify(valueOperations, times(2)).set(anyString(), anyString(), any(Duration.class));  // 캐시 저장 검증
    }
    @Test
    void testGetDailyStatistics_CacheHit() throws JsonProcessingException {
        // Mock Redis cache hit
        when(objectRedisTemplate.opsForValue().get(anyString())).thenReturn(statisticsResponseDto);

        // Call the method
        StatisticsResponseDto result = orderService.getDailyStatistics(1L, LocalDate.now());

        // Verify that DB was not accessed
        verify(statisticsRepository, never()).findByStoreIdAndDate(anyLong(), any(LocalDate.class));
        assertNotNull(result);
        assertEquals(1500, result.getTotalSales());
    }

    @Test
    void testGetDailyStatistics_CacheMiss() {
        // Redis 캐시 미스 처리
        when(objectRedisTemplate.opsForValue().get(anyString())).thenReturn(null);
        // DB에서 데이터 조회
        when(statisticsRepository.findByStoreIdAndDate(anyLong(), any(LocalDate.class)))
                .thenReturn(Optional.of(statistics));

        // 메서드 호출
        StatisticsResponseDto result = orderService.getDailyStatistics(1L, LocalDate.now());

        // DB 접근 검증
        verify(statisticsRepository).findByStoreIdAndDate(anyLong(), any(LocalDate.class));

        // opsForValue()는 여러 번 호출될 수 있으므로 최소 한 번 호출되었는지 검증
        verify(objectRedisTemplate, atLeastOnce()).opsForValue(); // opsForValue() 호출
        verify(valueOperations, times(1)).set(anyString(), anyString(), any()); // 캐시 업데이트 1회 검증

        // 결과 검증
        assertNotNull(result);
        assertEquals(1000, result.getTotalSales());
    }

    @Test
    void testGetMonthlyStatistics_CacheHit() throws JsonProcessingException {
        // 메서드 호출
        StatisticsResponseDto result = orderService.getMonthlyStatistics(1L, LocalDate.now().withDayOfMonth(1), LocalDate.now());


        verify(statisticsRepository, never()).findByStoreIdAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class));

        // 검증: 반환된 객체가 예상 값과 같은지 확인
        assertNotNull(result);
        assertEquals(1500, result.getTotalSales());
    }

    @Test
    void testGetMonthlyStatistics_CacheMiss() {
        // Mock Redis cache miss and DB hit
        when(objectRedisTemplate.opsForValue().get(anyString())).thenReturn(null);
        when(statisticsRepository.findByStoreIdAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(statistics));


        StatisticsResponseDto result = orderService.getMonthlyStatistics(1L, LocalDate.now().withDayOfMonth(1), LocalDate.now());


        verify(statisticsRepository, times(1)).findByStoreIdAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class));


        verify(objectRedisTemplate, atLeastOnce()).opsForValue();
        verify(valueOperations, times(1)).set(anyString(), anyString(), any());  // 캐시 업데이트 검증

        assertNotNull(result);
        assertEquals(1000, result.getTotalSales());
    }

    @Test
    void testGetDailyStatisticsForAllStores_CacheHit() throws JsonProcessingException {

        // 메소드 호출
        StatisticsResponseDto result = orderService.getDailyStatisticsForAllStores(LocalDate.now());

        // 데이터베이스 접근이 없었는지 확인 (캐시에서 가져와야 하므로)
        verify(statisticsRepository, never()).findAllByDate(any(LocalDate.class));

        // 반환된 데이터가 null이 아닌지, 그리고 예상 값이 맞는지 검증
        assertNotNull(result);
        assertEquals(1500, result.getTotalSales());
    }

    @Test
    void testGetDailyStatisticsForAllStores_CacheMiss() {

        // Redis 캐시 미스 처리
        when(objectRedisTemplate.opsForValue().get(anyString())).thenReturn(null);
        // DB에서 데이터 조회
        when(statisticsRepository.findAllByDate(any(LocalDate.class)))
                .thenReturn(Collections.singletonList(statistics));

        // 메서드 호출
        StatisticsResponseDto result = orderService.getDailyStatisticsForAllStores(LocalDate.now());

        // DB 접근 검증
        verify(statisticsRepository, times(1)).findAllByDate(any(LocalDate.class));

        // 캐시 업데이트 검증
        verify(objectRedisTemplate, atLeastOnce()).opsForValue();  // opsForValue()는 여러 번 호출될 수 있음
        verify(valueOperations, times(1)).set(anyString(), anyString(), any());  // 캐시 업데이트 1회 검증

        // 결과 검증
        assertNotNull(result);
        assertEquals(1000, result.getTotalSales());
    }

    @Test
    void testGetMonthlyStatisticsForAllStores_CacheHit() throws JsonProcessingException {
        // 메서드 호출
        StatisticsResponseDto result = orderService.getMonthlyStatisticsForAllStores(YearMonth.now());

       // (캐시에서 데이터를 가져온 경우 DB 접근이 없어야 함)
        verify(statisticsRepository, never()).findAllByDateBetween(any(LocalDate.class), any(LocalDate.class));

        // 검증: 반환된 데이터가 null이 아닌지, 그리고 총 매출이 1500인지 확인
        assertNotNull(result);
        assertEquals(1500, result.getTotalSales());
    }

    @Test
    void testGetMonthlyStatisticsForAllStores_CacheMiss() {
        // Redis 캐시 미스 처리
        when(objectRedisTemplate.opsForValue().get(anyString())).thenReturn(null);
        // DB에서 데이터 조회
        when(statisticsRepository.findAllByDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(statistics));

        // 메서드 호출
        StatisticsResponseDto result = orderService.getMonthlyStatisticsForAllStores(YearMonth.now());

        // DB 접근 검증
        verify(statisticsRepository, times(1)).findAllByDateBetween(any(LocalDate.class), any(LocalDate.class));

        // opsForValue() 호출이 여러 번 발생하므로 최소 한 번 이상 호출되었는지 검증
        verify(objectRedisTemplate, atLeastOnce()).opsForValue(); // opsForValue()는 여러 번 호출될 수 있음
        verify(valueOperations, times(1)).set(anyString(), anyString(), any()); // 캐시 업데이트 1회 검증

        // 결과 검증
        assertNotNull(result);
        assertEquals(1000, result.getTotalSales());
    }
}

