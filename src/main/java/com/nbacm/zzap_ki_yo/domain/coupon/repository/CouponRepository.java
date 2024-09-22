package com.nbacm.zzap_ki_yo.domain.coupon.repository;

import com.nbacm.zzap_ki_yo.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByUserId(Long userId);
    List<Coupon> findByStoreId(Long storeId);
}
