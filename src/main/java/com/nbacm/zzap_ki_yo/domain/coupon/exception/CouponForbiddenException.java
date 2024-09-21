package com.nbacm.zzap_ki_yo.domain.coupon.exception;

import com.nbacm.zzap_ki_yo.domain.exception.ForbiddenException;

public class CouponForbiddenException extends ForbiddenException {
    public CouponForbiddenException(String message) {
        super(message);
    }
}
