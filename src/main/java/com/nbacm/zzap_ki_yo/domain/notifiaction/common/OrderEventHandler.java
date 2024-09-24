package com.nbacm.zzap_ki_yo.domain.notifiaction.common;

import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class OrderEventHandler {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.add(emitter);
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        return emitter;
    }

    @EventListener
    public void handleOrderEvent(Order order) {
        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();

        this.emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(order.getOrderId()))
                        .name("order_update")
                        .data(order.toEventData()));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.removeAll(deadEmitters);
    }

}
