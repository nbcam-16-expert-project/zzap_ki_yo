package com.nbacm.zzap_ki_yo.domain.coupon.repository;

import com.nbacm.zzap_ki_yo.domain.coupon.entity.Coupon;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByUser(User user);
    List<Coupon> findByStore(Store store);
}
