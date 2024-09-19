package com.nbacm.zzap_ki_yo.domain.user.exception;

import com.nbacm.zzap_ki_yo.domain.exception.ForbiddenException;

public class AlreadyExistsException extends ForbiddenException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
