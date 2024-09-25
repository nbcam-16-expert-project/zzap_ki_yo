package com.nbacm.zzap_ki_yo.domain.order.exception;

import com.nbacm.zzap_ki_yo.domain.exception.ForbiddenException;

public class ClosedStoreException extends ForbiddenException {
    public ClosedStoreException(String message) {
        super(message);
    }
}
