package com.nbacm.zzap_ki_yo.domain.order.controller;

import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.service.OrderService;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    // 주문하기.
    @PostMapping("/stores/{storeId}/orders")
    public ResponseEntity<OrderSaveResponse> saveOrder(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody OrderSaveRequest orderSaveRequest
    ) {
        return ResponseEntity.ok(orderService.saveOrder(authUser, storeId, orderSaveRequest));
    }

    // 주문 내역 조회
    @GetMapping("/orders")
    public ResponseEntity<List<OrderSaveResponse>> getOrders(@Auth AuthUser authUser) {
        return ResponseEntity.ok(orderService.getOrdersByUser(authUser));
    }

    // 주문 상태 추적
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderSaveResponse> getOrder(@PathVariable Long orderId, @Auth AuthUser authUser) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, authUser));
    }

    // 주문 취소
}
