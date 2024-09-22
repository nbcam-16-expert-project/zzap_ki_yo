package com.nbacm.zzap_ki_yo.domain.coupon.controller;

import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponRequest;
import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponResponse;
import com.nbacm.zzap_ki_yo.domain.coupon.service.CouponService;
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
public class CouponController {

    private final CouponServiceImpl couponServiceImpl;

    // 쿠폰 생성(사장)
    @PostMapping("/stores/{storeId}/coupons")
    public ResponseEntity<CouponResponse> saveCoupon(
            @PathVariable Long storeId,
            @RequestBody CouponRequest couponRequest,
            @Auth AuthUser authUser
    ) {
        String email = authUser.getEmail();
        return ResponseEntity.ok(couponServiceImpl.saveCoupon(storeId, couponRequest, email));
    }

    // 보유 쿠폰 조회(유저)
    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> getAllCouponsByUser(
            @Auth AuthUser authUser
    ){
        String email = authUser.getEmail();
        return ResponseEntity.ok(couponServiceImpl.getAllCoupons(email));
    }

    // 발행한 쿠폰 조회(사장)
    @GetMapping("/stores/{storeId}/coupons")
    public ResponseEntity<List<CouponResponse>> getAllCouponsByStoreId(
            @Auth AuthUser authUser,
            @PathVariable Long storeId
    ){
        String email = authUser.getEmail();
        return ResponseEntity.ok(couponServiceImpl.getAllCouponsByStoreId(email, storeId));
    }

    // 특정 유저가 보유한 쿠폰 조회(관리자)

    // 특정 가게가 발생한 쿠폰 조회(관리자)

    // 쿠폰 발행취소(삭제, 사장)

    // 쿠폰 발행취소(삭제, 관리자)
}




























