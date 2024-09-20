package com.nbacm.zzap_ki_yo.domain.order.exeption;

import com.nbacm.zzap_ki_yo.domain.exception.ForbiddenException;

public class NotEnoughPriceException extends ForbiddenException {
    public NotEnoughPriceException(String message) {
        super(message);
    }
}
