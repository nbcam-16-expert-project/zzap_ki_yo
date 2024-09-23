package com.nbacm.zzap_ki_yo.domain.order.exception;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
