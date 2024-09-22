package com.nbacm.zzap_ki_yo.domain.coupon.entity;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    // 정액 쿠폰은 할인율을 100으로 하고 최대 할인 수치를 조절하면 됨.

    @Column(name = "min_price", nullable = false)
    private Integer minPrice;

    @Column(name = "max_discount")
    private Integer maxDiscount;
    //

    @Column(name = "coupon_status", nullable = false)
    private CouponStatus couponStatus;

    @Column(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "expiry_period", nullable = false)
    private Period expiryPeriod;
    // 사용법('일' 만 사용함. 일 단위로 유효기간 받기)
    /*Period period = Period.ofDays(10); // 10일
    Period periodInMonths = Period.ofMonths(3); // 3개월
    Period periodBetween = Period.between(startDate, endDate); // 두 날짜 사이의 간격*/

    @Column(name = "store_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Builder
    public Coupon(
            Integer discountRate,
            Integer minPrice,
            Integer maxDiscount,
            CouponStatus couponStatus,
            User user,
            Period expiryPeriod,
            Store store
    ){
        this.discountRate = discountRate;
        this.minPrice = minPrice;
        this.maxDiscount = maxDiscount;
        this.couponStatus = couponStatus;
        this.user = user;
        this.expiryPeriod = expiryPeriod;
        this.store = store;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDate.now();
    }

    public void deActivated(){
        this.couponStatus = CouponStatus.UNUSABLE;
    }
}
