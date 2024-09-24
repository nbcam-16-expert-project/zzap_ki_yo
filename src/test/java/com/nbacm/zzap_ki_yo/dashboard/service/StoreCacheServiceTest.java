package com.nbacm.zzap_ki_yo.dashboard.service;


import com.nbacm.zzap_ki_yo.domain.dashboard.dto.StatisticsResponseDto;
import com.nbacm.zzap_ki_yo.domain.order.service.OrderServiceImpl;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.store.service.AdminStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoreCacheServiceTest {
    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private AdminStoreServiceImpl adminStoreService;

    private User adminUser;
    private User ownerUser;
    private Store store;
    private StatisticsResponseDto statisticsResponse;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .email("admin@email.com")
                .userRole(UserRole.ADMIN)
                .build();

        ownerUser = User.builder()
                .email("owner@email.com")
                .userRole(UserRole.OWNER)
                .build();

        store = Store.builder()
                .storeName("Test Store")
                .user(ownerUser)
                .build();
        // Reflection을 통해 storeId 설정
        setStoreId(store, 1L);

        statisticsResponse = new StatisticsResponseDto(100000, 1000); // Example response
    }
    // Reflection으로 storeId 설정하는 메서드
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
    void testGetDailyStatistics_Success() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(adminUser);
        when(storeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(store));
        when(orderService.getDailyStatistics(anyLong(), any(LocalDate.class))).thenReturn(statisticsResponse);

        StatisticsResponseDto result = adminStoreService.getDailyStatistics(store.getStoreId(), adminUser.getEmail());

        assertNotNull(result);
        assertEquals(statisticsResponse, result);
        verify(orderService, times(1)).getDailyStatistics(anyLong(), any(LocalDate.class));
    }

    @Test
    void testGetDailyStatistics_StoreNotFound() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(adminUser);
        when(storeRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> adminStoreService.getDailyStatistics(store.getStoreId(), adminUser.getEmail()));
        verify(orderService, never()).getDailyStatistics(anyLong(), any(LocalDate.class));
    }

    @Test
    void testGetMonthlyStatistics_Success() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(adminUser);
        when(storeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(store));
        when(orderService.getMonthlyStatistics(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(statisticsResponse);

        StatisticsResponseDto result = adminStoreService.getMonthlyStatistics(store.getStoreId(), adminUser.getEmail());

        assertNotNull(result);
        assertEquals(statisticsResponse, result);
        verify(orderService, times(1)).getMonthlyStatistics(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testGetMonthlyStatistics_StoreNotFound() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(adminUser);
        when(storeRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> adminStoreService.getMonthlyStatistics(store.getStoreId(), adminUser.getEmail()));
        verify(orderService, never()).getMonthlyStatistics(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testGetDailyAllStatistics_Success() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(adminUser);
        when(orderService.getDailyStatisticsForAllStores(any(LocalDate.class))).thenReturn(statisticsResponse);

        StatisticsResponseDto result = adminStoreService.getDailyAllStatistics(adminUser.getEmail());

        assertNotNull(result);
        assertEquals(statisticsResponse, result);
        verify(orderService, times(1)).getDailyStatisticsForAllStores(any(LocalDate.class));
    }

    @Test
    void testGetDailyAllStatistics_Unauthorized() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(ownerUser);  // 권한이 ADMIN이 아닌 경우

        assertThrows(UnauthorizedException.class, () -> adminStoreService.getDailyAllStatistics(ownerUser.getEmail()));
        verify(orderService, never()).getDailyStatisticsForAllStores(any(LocalDate.class));
    }

    @Test
    void testGetMonthlyAllStatistics_Success() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(adminUser);
        when(orderService.getMonthlyStatisticsForAllStores(any(YearMonth.class))).thenReturn(statisticsResponse);

        StatisticsResponseDto result = adminStoreService.getMonthlyAllStatistics(adminUser.getEmail());

        assertNotNull(result);
        assertEquals(statisticsResponse, result);
        verify(orderService, times(1)).getMonthlyStatisticsForAllStores(any(YearMonth.class));
    }

    @Test
    void testGetMonthlyAllStatistics_Unauthorized() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(ownerUser);  // 권한이 ADMIN이 아닌 경우

        assertThrows(UnauthorizedException.class, () -> adminStoreService.getMonthlyAllStatistics(ownerUser.getEmail()));
        verify(orderService, never()).getMonthlyStatisticsForAllStores(any(YearMonth.class));
    }
}
