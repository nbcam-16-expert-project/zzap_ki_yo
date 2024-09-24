package com.nbacm.zzap_ki_yo.notification;
import com.nbacm.zzap_ki_yo.domain.notification.common.OrderEventHandler;
import com.nbacm.zzap_ki_yo.domain.notification.controller.NotificationController;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;


public class NotificationControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderEventHandler orderEventHandler;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    void testStreamOrders() throws Exception {
        // Mock SseEmitter 생성
        SseEmitter sseEmitter = new SseEmitter();
        when(orderEventHandler.createEmitter()).thenReturn(sseEmitter);

        // /api/v1/orders/stream 엔드포인트에 대한 GET 요청을 보냄
        mockMvc.perform(get("/api/v1/orders/stream")
                        .accept(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(status().isOk());  // HTTP 200 상태 확인
    }

}
