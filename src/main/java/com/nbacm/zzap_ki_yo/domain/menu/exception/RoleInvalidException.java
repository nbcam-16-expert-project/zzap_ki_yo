package com.nbacm.zzap_ki_yo.domain.menu.exception;

import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;

public class RoleInvalidException extends UnauthorizedException {

    public RoleInvalidException(String message) {
        super(message);
    }

}
