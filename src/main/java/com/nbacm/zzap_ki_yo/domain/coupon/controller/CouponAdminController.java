package com.nbacm.zzap_ki_yo.domain.coupon.controller;

import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponRequest;
import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponResponse;
import com.nbacm.zzap_ki_yo.domain.coupon.service.CouponServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CouponAdminController {

    private final CouponServiceImpl couponServiceImpl;

    // 쿠폰 생성
    @PostMapping("/admin/stores/{storeId}/coupons")
    public ResponseEntity<CouponResponse> saveCouponAdmin(
            @PathVariable Long storeId,
            @RequestBody CouponRequest couponRequest,
            @Auth AuthUser authUser
    ) {
        String email = authUser.getEmail();
        return ResponseEntity.ok(couponServiceImpl.saveCoupon(storeId, couponRequest, email));
    }

    // 특정 유저가 보유한 쿠폰 조회
    @GetMapping("/coupons/admin/{userId}")
    public ResponseEntity<List<CouponResponse>> getAllCoupons(
            @Auth AuthUser authUser,
            @PathVariable Long userId
    ){
        String email = authUser.getEmail();
        return ResponseEntity.ok(couponServiceImpl.getCouponByUser(email, userId));
    }

    // 특정 가게가 발행한 쿠폰 조회
    @GetMapping("/stores/{storeId}/admin/coupons")
    public ResponseEntity<List<CouponResponse>> getAllCouponsByStoreIdAdmin(
            @Auth AuthUser authUser,
            @PathVariable Long storeId
    ){
        String email = authUser.getEmail();
        return ResponseEntity.ok(couponServiceImpl.getAllCouponsByStoreIdAdmin(email, storeId));
    }

    // 쿠폰 삭제
    @DeleteMapping("/stores/{storeId}/admin/coupons/{couponId}")
    public void deleteCoupon(
            @Auth AuthUser authUser,
            @PathVariable Long couponId
    ) {
        String email = authUser.getEmail();
        couponServiceImpl.deleteCouponAdmin(email, couponId);
    }
}































