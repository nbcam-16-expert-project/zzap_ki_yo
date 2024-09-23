package com.nbacm.zzap_ki_yo.domain.coupon.service;

import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponRequest;
import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponResponse;

import java.util.List;

public interface CouponService {
    // 쿠폰 생성(사장)
    CouponResponse saveCoupon(Long storeId, CouponRequest couponRequest, String email);

    // 보유 쿠폰 조회(유저)
    List<CouponResponse> getAllCoupons(String email);

    // 발행한 쿠폰 조회(사장)
    List<CouponResponse> getAllCouponsByStoreId(String email, Long storeId);

    // 특정 유저가 보유한 쿠폰 조회(관리자)
    List<CouponResponse> getCouponByUser(String email, Long userId);

    // 특정 가게가 발생한 쿠폰 조회(관리자)
    List<CouponResponse> getAllCouponsByStoreIdAdmin (String email, Long storeId);

    // 쿠폰 발행취소(삭제, 사장)
    void deleteCoupon(String email, Long storeId, Long couponId);

    // 쿠폰 발행취소(삭제, 관리자)
    void deleteCouponAdmin(String email, Long couponId);
}
