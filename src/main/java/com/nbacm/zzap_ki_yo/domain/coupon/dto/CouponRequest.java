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
    private Integer expiryPeriod;
    // 유효기간은 '일' 기준
    // 예) 12345를 넣으면 유효기간이 12,345(만 이천 삼백 사십 오)일로 설정됨
}
