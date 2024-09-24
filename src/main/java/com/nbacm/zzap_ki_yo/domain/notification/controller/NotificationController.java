package com.nbacm.zzap_ki_yo.domain.notification.controller;

import com.nbacm.zzap_ki_yo.domain.notification.common.OrderEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class NotificationController {
    private final OrderEventHandler orderEventHandler;
    @GetMapping(value = "/orders/stream", produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.ALL_VALUE})
    public SseEmitter streamOrders() {
        return orderEventHandler.createEmitter();
    }

}
