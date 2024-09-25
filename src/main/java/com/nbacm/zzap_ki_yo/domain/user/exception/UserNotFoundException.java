package com.nbacm.zzap_ki_yo.domain.user.exception;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
