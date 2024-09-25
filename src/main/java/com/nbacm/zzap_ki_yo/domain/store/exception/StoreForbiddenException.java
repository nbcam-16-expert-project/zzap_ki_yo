package com.nbacm.zzap_ki_yo.domain.store.exception;

import com.nbacm.zzap_ki_yo.domain.exception.ForbiddenException;

public class StoreForbiddenException extends ForbiddenException {
    public StoreForbiddenException(String message) {
        super(message);
    }
}

