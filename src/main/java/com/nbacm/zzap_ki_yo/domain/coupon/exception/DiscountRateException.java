package com.nbacm.zzap_ki_yo.domain.coupon.exception;

import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;

public class DiscountRateException extends BadRequestException {
    public DiscountRateException(String message) {
        super(message);
    }
}
