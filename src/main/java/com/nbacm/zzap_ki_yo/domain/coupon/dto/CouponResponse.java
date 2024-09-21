package com.nbacm.zzap_ki_yo.domain.coupon.dto;

import lombok.Getter;

@Getter
public class CouponResponse {
    private final String couponName;
    private final Integer discountRate;
    private final Integer minPrice;
    private final Integer maxDiscount;
    private final Long userId;
    private final String expiryPeriod;
    private final Long storeId;

    public CouponResponse(String couponName, Integer discountRate, Integer minPrice, Integer maxDiscount, Long userId, String expiryPeriod, Long storeId) {
        this.couponName = couponName;
        this.discountRate = discountRate;
        this.minPrice = minPrice;
        this.maxDiscount = maxDiscount;
        this.userId = userId;
        this.expiryPeriod = expiryPeriod;
        this.storeId = storeId;
    }
}
