package com.nbacm.zzap_ki_yo.domain.coupon.exception;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;

public class CouponNotFoundException extends NotFoundException {
    public CouponNotFoundException(String message) {
        super(message);
    }
}
