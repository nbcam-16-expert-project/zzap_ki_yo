package com.nbacm.zzap_ki_yo.domain.order.exeption;

import com.nbacm.zzap_ki_yo.domain.exception.ForbiddenException;

public class ClosedStoreException extends ForbiddenException {
    public ClosedStoreException(String message) {
        super(message);
    }
}
