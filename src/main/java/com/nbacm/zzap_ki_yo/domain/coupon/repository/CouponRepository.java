package com.nbacm.zzap_ki_yo.domain.coupon.repository;

import com.nbacm.zzap_ki_yo.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
