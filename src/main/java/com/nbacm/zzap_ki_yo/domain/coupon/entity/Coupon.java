package com.nbacm.zzap_ki_yo.domain.coupon.entity;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;

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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expiry_period", nullable = false)
    private Period expiryPeriod;

    @Column(name = "store_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    // 사용법
    /*Period period = Period.ofDays(10); // 10일
    Period periodInMonths = Period.ofMonths(3); // 3개월
    Period periodBetween = Period.between(startDate, endDate); // 두 날짜 사이의 간격*/

    @Builder
    public Coupon(
            String Id,
            Integer discountRate,
            Integer minPrice,
            Integer maxDiscount,
            CouponStatus couponStatus,
            User user,
            LocalDateTime createdAt,
            Period expiryPeriod,
            Store store
    ){
        this.couponName = Id;
        this.discountRate = discountRate;
        this.minPrice = minPrice;
        this.maxDiscount = maxDiscount;
        this.couponStatus = couponStatus;
        this.user = user;
        this.createdAt = createdAt;
        this.expiryPeriod = expiryPeriod;
        this.store = store;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
