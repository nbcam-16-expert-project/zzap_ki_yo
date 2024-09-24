package com.nbacm.zzap_ki_yo.domain.coupon.service;

import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponRequest;
import com.nbacm.zzap_ki_yo.domain.coupon.dto.CouponResponse;
import com.nbacm.zzap_ki_yo.domain.coupon.entity.Coupon;
import com.nbacm.zzap_ki_yo.domain.coupon.exception.CouponForbiddenException;
import com.nbacm.zzap_ki_yo.domain.coupon.repository.CouponRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
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
        setField(store, "storeId", storeId);

        List<Store> stores = new ArrayList<>();
        stores.add(store);
        setField(publisher, "stores", stores);

        given(userRepository.findByEmailOrElseThrow(email)).willReturn(publisher);
        given(userRepository.findById(couponRequest.getUserId())).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        CouponResponse couponResponse = service.saveCoupon(storeId, couponRequest, email);

        // then
        assertNotNull(couponResponse);
    }

    @Test
    void 발행자가해당가게의사장이맞는지확인(){
        //g
        Long storeId = 1L;

        CouponRequest couponRequest = new CouponRequest();
        setField(couponRequest, "couponName", "Test Coupon");
        setField(couponRequest, "discountRate", 20);
        setField(couponRequest, "minPrice", 1000);
        setField(couponRequest, "maxDiscount", 5000);
        setField(couponRequest, "userId", 1L);
        setField(couponRequest, "expiryPeriod", 30);

        String email = "test@test.com";


        //이메일에서 얻어온 스토어아이디랑 바로 받은 스토어아이디랑 달라야함
        User publisher = new User();
        setField(publisher, "userRole", UserRole.OWNER);
        setField(publisher, "email", email);
        List<Store> stores = new ArrayList<>();
        Store ownedStore = new Store();
        setField(ownedStore, "storeId", 2L);
        stores.add(ownedStore);
        setField(publisher, "stores", stores);

        User user = new User();

        Store serchedStore = new Store();
        setField(serchedStore, "storeId", storeId);
        // 유저 - 그냥 유저
        // 퍼블리셔 - 2번가게 소유, 제공 이메일을 갖고있음, 유저롤 오너
        // 발행할 가게 - 1번, 제공된 아이디를 갖고있음

        given(userRepository.findByEmailOrElseThrow(email)).willReturn(publisher);
        given(userRepository.findById(couponRequest.getUserId())).willReturn(Optional.of(user));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(serchedStore));

        //w
        CouponForbiddenException exception = assertThrows(CouponForbiddenException.class,
                () -> service.saveCoupon(storeId, couponRequest, email));

        //t
        assertEquals("해당 가게의 소유자만 쿠폰을 발행할 수 있습니다.",exception.getMessage());
    }

    @Test
    void getAllCoupons() {
        // g
        String email = "test@test.com";

        User user = new User();
        setField(user, "userId", 1L);
        setField(user, "email", email);

        Store store = new Store();

        Coupon coupon = new Coupon();
        setField(coupon, "couponName", "Test Coupon");
        setField(coupon, "discountRate", 20);
        setField(coupon, "minPrice", 1000);
        setField(coupon, "maxDiscount", 5000);
        setField(coupon, "user", user);
        setField(coupon, "expiryPeriod", Period.ofDays(30));
        setField(coupon, "store", store);

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(coupon);

        given(userRepository.findByEmailOrElseThrow(email)).willReturn(user);
        given(couponRepository.findByUser(user)).willReturn(coupons);
        // w
        List<CouponResponse> responseList = service.getAllCoupons(email);
        // t
        assertNotNull(responseList);
    }

    @Test
    void deleteCoupon() {
        //g
        Long storeId = 1L;
        Store store = new Store();
        setField(store, "storeId", storeId);
        List<Store> stores = new ArrayList<>();
        stores.add(store);

        String email = "test@test.com";
        User user = new User();
        setField(user, "email", email);
        setField(user, "stores", stores);

        Long couponId = 1L;
        Coupon coupon = new Coupon();
        setField(coupon, "id", couponId);

        //w
        couponRepository.save(coupon);
        couponRepository.delete(coupon);

        //t
        assertEquals(0, couponRepository.count());
    }
}






























