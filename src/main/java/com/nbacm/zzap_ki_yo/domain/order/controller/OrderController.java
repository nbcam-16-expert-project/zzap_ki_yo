package com.nbacm.zzap_ki_yo.domain.order.controller;

import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderUpdateRequest;
import com.nbacm.zzap_ki_yo.domain.order.service.OrderServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    // 주문하기.
    @PostMapping("/stores/{storeId}/orders")
    public ResponseEntity<OrderSaveResponse> saveOrder(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody OrderSaveRequest orderSaveRequest
    ) {
        String email = authUser.getEmail();
        return ResponseEntity.ok(orderServiceImpl.saveOrder(email, storeId, orderSaveRequest));
    }

    // 주문 내역 조회
    @GetMapping("/orders")
    public ResponseEntity<List<OrderSaveResponse>> getOrders(@Auth AuthUser authUser) {
        String email = authUser.getEmail();
        return ResponseEntity.ok(orderServiceImpl.getOrdersByUser(email));
    }

    // 주문 내역 조회(관리자)
    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrderSaveResponse>> getOrdersByUserId(@Auth AuthUser authUser, @PathVariable Long userId) {
        String email = authUser.getEmail();
        return ResponseEntity.ok(orderServiceImpl.getOrdersByUserAdmin(email, userId));
    }

    // 주문 상태 추적
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderSaveResponse> getOrder(@PathVariable Long orderId, @Auth AuthUser authUser) {
        String email = authUser.getEmail();
        return ResponseEntity.ok(orderServiceImpl.getOrderById(orderId, email));
    }

    // 주문 취소
    @DeleteMapping("/orders/{orderId}")
    public void deleteOrder(@PathVariable Long orderId, @Auth AuthUser authUser) {
        String email = authUser.getEmail();
        orderServiceImpl.deleteOrderById(orderId, email);
    }

    // 주문 상태 변경(사장)
    @PutMapping("/stores/{storeId}/orders/{orderId}")
    public void updateOrder(
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @RequestBody OrderUpdateRequest orderUpdateRequest,
            @Auth AuthUser authUser
    ) {
        String email = authUser.getEmail();
        orderServiceImpl.updateOrder(storeId, orderId, orderUpdateRequest, email);
    }
}
