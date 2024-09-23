package com.nbacm.zzap_ki_yo.domain.coupon.service;

import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponRequest;
import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponResponse;
import com.nbacm.zzap_ki_yo.domain.coupon.repository.CouponRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @InjectMocks
    private CouponServiceImpl service;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;

    @Test
    void saveCoupon() {
        // given
        Long storeId = 1L;

        CouponRequest couponRequest = new CouponRequest();
        setField(couponRequest, "couponName", "Test Coupon");
        setField(couponRequest, "discountRate", 20);
        setField(couponRequest, "minPrice", 1000);
        setField(couponRequest, "maxDiscount", 5000);
        setField(couponRequest, "userId", 1L);
        setField(couponRequest, "expiryPeriod", 30);

        String email = "test@test.com";

        User publisher = new User();
        setField(publisher, "email", email);

        User user = new User();
        Store store = new Store();
        setField(publisher, "store", store);

        given(userRepository.findByEmailOrElseThrow(email)).willReturn(publisher);
        given(userRepository.findById(couponRequest.getUserId())).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        CouponResponse couponResponse = service.saveCoupon(storeId, couponRequest, email);

        // then
        assertNotNull(couponResponse);

    }
}