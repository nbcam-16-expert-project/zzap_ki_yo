package com.nbacm.zzap_ki_yo.domain.store.exception;

import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;

public class StoreBadRequestException extends BadRequestException {
    public StoreBadRequestException(String message) {
        super(message);
    }
}
