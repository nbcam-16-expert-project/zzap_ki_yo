package com.nbacm.zzap_ki_yo.notification;

import com.nbacm.zzap_ki_yo.domain.notification.common.OrderEventHandler;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderEventHandlerTest {

    @InjectMocks
    private OrderEventHandler orderEventHandler;  // 실제로 테스트할 클래스

    @Captor
    private ArgumentCaptor<SseEmitter.SseEventBuilder> eventCaptor;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // 목(mock) 객체를 초기화
    }

    @Test
    void testCreateEmitter() {
        // Emitter 생성 테스트
        SseEmitter emitter = orderEventHandler.createEmitter();
        assertNotNull(emitter);
        assertEquals(1, orderEventHandler.getEmitters().size());
    }

    @Test
    void testHandleOrderEvent() throws IOException {
        OrderEventHandler orderEventHandler = new OrderEventHandler();

        // Mock SseEmitter 생성
        SseEmitter emitter = mock(SseEmitter.class);

        // Mock SseEventBuilder 생성
        SseEmitter.SseEventBuilder eventBuilder = mock(SseEmitter.SseEventBuilder.class);

        // SseEmitter.event() 메서드가 mock SseEventBuilder를 반환하도록 설정
        try (MockedStatic<SseEmitter> mockedStatic = mockStatic(SseEmitter.class)) {
            mockedStatic.when(SseEmitter::event).thenReturn(eventBuilder);

            // eventBuilder 메서드 체이닝 설정
            when(eventBuilder.id(anyString())).thenReturn(eventBuilder);
            when(eventBuilder.name(anyString())).thenReturn(eventBuilder);
            when(eventBuilder.data(anyString())).thenReturn(eventBuilder);

            // emitter를 OrderEventHandler에 추가
            orderEventHandler.getEmitters().add(emitter);

            // Mock Order 객체 생성
            Order order = mock(Order.class);
            when(order.getOrderId()).thenReturn(1L);
            when(order.toEventData()).thenReturn("Order Data");

            // 이벤트 처리
            orderEventHandler.handleOrderEvent(order);

            // 이벤트 빌더 메서드 호출 검증
            verify(eventBuilder).id("1");
            verify(eventBuilder).name("order_update");
            verify(eventBuilder).data("Order Data");

            // emitter.send() 메서드가 호출되었는지 확인
            verify(emitter).send(eventBuilder);
        }
    }

    @Test
    void testHandleOrderEventWithIOException() throws IOException {
        // Mock Emitter 생성 및 IOException 발생 설정
        SseEmitter emitter = mock(SseEmitter.class);
        orderEventHandler.getEmitters().add(emitter);

        // Mock Order 객체 생성
        Order order = mock(Order.class);
        when(order.getOrderId()).thenReturn(1L);
        when(order.toEventData()).thenReturn("Order Data");

        // IOException 발생하도록 설정
        doThrow(new IOException()).when(emitter).send(any(SseEmitter.SseEventBuilder.class));

        // 이벤트 처리 (IOException 발생)
        orderEventHandler.handleOrderEvent(order);

        // IOException으로 인해 emitter가 리스트에서 제거되었는지 확인
        assertEquals(0, orderEventHandler.getEmitters().size());
    }
}
