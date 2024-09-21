package com.nbacm.zzap_ki_yo.domain.coupon.dto;

import com.nbacm.zzap_ki_yo.domain.coupon.entity.CouponStatus;
import lombok.Getter;

@Getter
public class CouponRequest {
    private String couponName;
    private Integer discountRate;
    private Integer minPrice;
    private Integer maxDiscount;
    private CouponStatus couponStatus;
    private Long userId;
    private String expiryPeriod;
}
