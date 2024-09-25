package com.nbacm.zzap_ki_yo.domain.store.exception;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;

public class StoreNotFoundException extends NotFoundException {
    public StoreNotFoundException(String message) {
        super(message);
    }
}

