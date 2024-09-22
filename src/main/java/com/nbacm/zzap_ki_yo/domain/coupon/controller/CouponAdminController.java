package com.nbacm.zzap_ki_yo.domain.coupon.controller;

import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponRequest;
import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponResponse;
import com.nbacm.zzap_ki_yo.domain.coupon.service.CouponServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores/{storeId}/coupons/admin")
public class CouponAdminController {

    private final CouponServiceImpl couponServiceImpl;

    // 쿠폰 생성
    @PostMapping
    public ResponseEntity<CouponResponse> saveCouponAdmin(
            @PathVariable Long storeId,
            @RequestBody CouponRequest couponRequest,
            @Auth AuthUser authUser
    ) {
        String email = authUser.getEmail();
        return ResponseEntity.ok(couponServiceImpl.saveCouponAdmin(storeId, couponRequest, email));
    }

    // 특정 유저가 보유한 쿠폰 조회

    // 특정 가게가 발행한 쿠폰 조회

    // 쿠폰 삭제
}
