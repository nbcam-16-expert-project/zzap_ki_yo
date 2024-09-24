package com.nbacm.zzap_ki_yo.domain.store.exception;

import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;

public class StoreUnauthorizedException extends UnauthorizedException {
    public StoreUnauthorizedException(String message) {
        super(message);
    }
}
