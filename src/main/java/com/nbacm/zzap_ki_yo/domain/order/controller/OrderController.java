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
    // 롱 userid는 임시.
    // 로그인 쪽 완성되면 잊지 말고 헤더에서 '로그인된 유저'를 받게 바꾸기
    @PostMapping("/stores/{storeId}/orders")
    public ResponseEntity<OrderSaveResponse> saveOrder(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody OrderSaveRequest orderSaveRequest
    ) {
        return ResponseEntity.ok(orderService.saveOrder(authUser, storeId, orderSaveRequest));
    }

    // 주문 내역 조회  // 로그인 쪽 완성되면 잊지 말고 헤더에서 '로그인된 유저'를 받게 바꾸기
    @GetMapping("/orders")
    public ResponseEntity<List<OrderSaveResponse>> getOrders(Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    // 주문 상태 추적

    // 주문 취소
}
