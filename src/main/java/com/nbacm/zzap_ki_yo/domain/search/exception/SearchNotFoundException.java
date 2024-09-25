package com.nbacm.zzap_ki_yo.domain.search.exception;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;

public class SearchNotFoundException extends NotFoundException {
    public SearchNotFoundException(String message) {
        super(message);
    }
}
