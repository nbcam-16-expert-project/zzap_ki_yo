package com.nbacm.zzap_ki_yo.domain.user.exception;

import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;

public class InvalidRoleException extends UnauthorizedException {
    public InvalidRoleException(String message) {
        super(message);
    }
}
