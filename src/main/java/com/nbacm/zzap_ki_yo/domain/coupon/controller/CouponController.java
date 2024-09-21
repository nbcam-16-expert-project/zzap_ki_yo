package com.nbacm.zzap_ki_yo.domain.coupon.controller;

import com.nbacm.zzap_ki_yo.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 생성(사장)

    // 쿠폰 생성(관리자)

    // 보유 쿠폰 조회(유저)

    // 발행한 쿠폰 조회(사장)

    // 특정 유저가 보유한 쿠폰 조회(관리자)

    // 특정 가게가 발생한 쿠폰 조회(관리자)

    // 쿠폰 발행취소(삭제, 사장)

    // 쿠폰 발행취소(삭제, 관리자)
}
