package com.nbacm.zzap_ki_yo.domain.coupon.service;

import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponRequest;
import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponResponse;
import com.nbacm.zzap_ki_yo.domain.coupon.entity.Coupon;
import com.nbacm.zzap_ki_yo.domain.coupon.entity.CouponStatus;
import com.nbacm.zzap_ki_yo.domain.coupon.exception.CouponForbiddenException;
import com.nbacm.zzap_ki_yo.domain.coupon.exception.DiscountRateException;
import com.nbacm.zzap_ki_yo.domain.coupon.repository.CouponRepository;
import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 쿠폰 생성(사장)
    @Transactional
    @Override
    public CouponResponse saveCoupon(Long storeId, CouponRequest couponRequest, String email){

        User owner = userRepository.findByEmailOrElseThrow(email);

        User user = userRepository.findById(couponRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("해당 쿠폰 발급 대상 유저는 없는 유저입니다"));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("해당 가게는 없는 가게입니다."));

        // 발행자가 해당 가게의 사장이 맞는지 확인
        for (Store ownstore : owner.getStores()){
            int i = 0;
            if(!ownstore.equals(store)){
                i++;
            }
            if (owner.getStores().size() == i){
                throw new CouponForbiddenException("해당 가게의 소유자만 쿠폰을 발행할 수 있습니다.");
            }
        }

        // 할인율 0~100사이인지 확인
        if(couponRequest.getDiscountRate().intValue()>100 || couponRequest.getDiscountRate().intValue()<0){
            throw new DiscountRateException("할인율(백분율)은 0에서 100 사이의 정수여야 합니다.");
        }

        Coupon coupon = Coupon.builder()
                .discountRate(couponRequest.getDiscountRate())
                .minPrice(couponRequest.getMinPrice())
                .maxDiscount(couponRequest.getMaxDiscount())
                .couponStatus(CouponStatus.USABLE)
                .user(user)
                .expiryPeriod(Period.ofDays(couponRequest.getExpiryPeriod()))
                .store(store)
                .build();

        couponRepository.save(coupon);

        CouponResponse couponResponse = CouponResponse.createCouponResponse(coupon);

        return couponResponse;
    }

    // 쿠폰 생성(관리자)
    @Transactional
    @Override
    public CouponResponse saveCouponAdmin(Long storeId, CouponRequest couponRequest, String email) {

        User user = userRepository.findById(couponRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("해당 쿠폰 발급 대상 유저는 없는 유저입니다"));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("해당 가게는 없는 가게입니다."));

        // 할인율 0~100사이인지 확인
        if(couponRequest.getDiscountRate().intValue()>100 || couponRequest.getDiscountRate().intValue()<0){
            throw new DiscountRateException("할인율(백분율)은 0에서 100 사이의 정수여야 합니다.");
        }

        Coupon coupon = Coupon.builder()
                .discountRate(couponRequest.getDiscountRate())
                .minPrice(couponRequest.getMinPrice())
                .maxDiscount(couponRequest.getMaxDiscount())
                .couponStatus(CouponStatus.USABLE)
                .user(user)
                .expiryPeriod(Period.ofDays(couponRequest.getExpiryPeriod()))
                .store(store)
                .build();

        couponRepository.save(coupon);

        CouponResponse couponResponse = CouponResponse.createCouponResponse(coupon);

        return couponResponse;
    }

    // 보유 쿠폰 조회(유저)
    @Override
    public List<CouponResponse> getAllCoupons(String email) {

        User user = userRepository.findByEmailOrElseThrow(email);

        List<Coupon> couponList = couponRepository.findByUserId(user.getUserId());

        List<CouponResponse> couponResponseList = new ArrayList<>();
        for(Coupon coupon : couponList){
            couponResponseList.add(CouponResponse.createCouponResponse(coupon));
        }

        return couponResponseList;
    }

    // 발행한 쿠폰 조회(사장)
    @Override
    public CouponResponse getCouponOwner(Long couponId, String email) {
        return null;
    }

    // 특정 유저가 보유한 쿠폰 조회(관리자)
    public List<CouponResponse> getCouponByUser(String email, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("해당 유저는 존재하지 않습니다."));

        List<Coupon> couponList = couponRepository.findByUserId(userId);

        List<CouponResponse> couponResponseList = new ArrayList<>();
        for(Coupon coupon : couponList){
            couponResponseList.add(CouponResponse.createCouponResponse(coupon));
        }

        return couponResponseList;
    }

    // 특정 가게가 발생한 쿠폰 조회(관리자)

    // 쿠폰 발행취소(삭제, 사장)

    // 쿠폰 발행취소(삭제, 관리자)
}

































