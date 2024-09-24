package com.nbacm.zzap_ki_yo.dashboard.controller;
import com.nbacm.zzap_ki_yo.domain.dashboard.controller.DashBoardController;
import com.nbacm.zzap_ki_yo.domain.dashboard.dto.StatisticsResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.service.AdminStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DashBoardControllerTest {


    private MockMvc mockMvc;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private AdminStoreServiceImpl adminStoreService;

    @InjectMocks
    private DashBoardController dashBoardController;

    private StatisticsResponseDto statisticsResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(dashBoardController)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().equals(AuthUser.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return new AuthUser("admin@email.com", UserRole.ADMIN);
                    }
                })
                .build();

        statisticsResponseDto = new StatisticsResponseDto(1000, 10);
    }

    @Test
    void testGetDailyStatistics() throws Exception {
        when(adminStoreService.getDailyStatistics(anyLong(), any(String.class)))
                .thenReturn(statisticsResponseDto);

        mockMvc.perform(get("/api/v1/stores/1/statistics/daily")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"totalSales\":1000,\"customerCount\":10}"));
    }

    @Test
    void testGetMonthlyStatistics() throws Exception {
        // Mock the service call to return the statisticsResponseDto
        when(adminStoreService.getMonthlyStatistics(anyLong(), any(String.class)))
                .thenReturn(statisticsResponseDto);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/v1/stores/1/statistics/monthly")
                        .accept(MediaType.APPLICATION_JSON))  // Accept 헤더로 응답 타입을 JSON으로 요청
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"totalSales\":1000,\"customerCount\":10}"));
    }

    @Test
    void testGetDailyAllStatistics() throws Exception {
        // Mock the service call to return the statisticsResponseDto
        when(adminStoreService.getDailyAllStatistics(any(String.class)))
                .thenReturn(statisticsResponseDto);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/v1/stores/daily-all")
                        .accept(MediaType.APPLICATION_JSON))  // Accept 헤더로 응답 타입을 JSON으로 요청
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"totalSales\":1000,\"customerCount\":10}"));
    }

    @Test
    void testGetMonthlyAllStatistics() throws Exception {
        // Mock the service call to return the statisticsResponseDto
        when(adminStoreService.getMonthlyAllStatistics(any(String.class)))
                .thenReturn(statisticsResponseDto);

        // Perform the GET request and verify the response
        MvcResult result = mockMvc.perform(get("/api/v1/stores/monthly-all")
                        .accept(MediaType.APPLICATION_JSON))  // Accept 헤더로 응답 타입을 JSON으로 요청
                .andExpect(status().isOk()).andReturn();

        // 응답 정보 출력
        System.out.println(result.getResponse().getHeaders("Content-Type"));
        System.out.println("result = " + result.getResponse().getHeaderNames());
        System.out.println("result = " + result.getResponse().getContentType());
        System.out.println("result = " + result.getResponse().getContentLength());
        System.out.println("result = " + result.getResponse().getContentAsString());

        // 검증 추가
        mockMvc.perform(get("/api/v1/stores/monthly-all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"totalSales\":1000,\"customerCount\":10}"));
    }
}
