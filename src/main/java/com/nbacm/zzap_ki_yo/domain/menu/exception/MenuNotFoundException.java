package com.nbacm.zzap_ki_yo.domain.menu.exception;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;

public class MenuNotFoundException extends NotFoundException {
    public MenuNotFoundException(String message) {
        super(message);
    }
}
