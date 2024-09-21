package com.nbacm.zzap_ki_yo.domain.coupon.entity;

import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "coupons")
@NoArgsConstructor
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_name", nullable = false)
    private String couponName;

    @Column(name = "discount_rate", nullable = false)
    private Integer discountRate;

    @Column(name = "min_price", nullable = false)
    private Integer minPrice;

    @Column(name = "max_discount")
    private Integer maxDiscount;

    @Column(name = "coupon_status", nullable = false)
    private CouponStatus couponStatus;

    @Column(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
