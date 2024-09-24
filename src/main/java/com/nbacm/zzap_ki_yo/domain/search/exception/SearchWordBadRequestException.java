package com.nbacm.zzap_ki_yo.domain.search.exception;

import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;

public class SearchWordBadRequestException extends BadRequestException {
    public SearchWordBadRequestException(String message) {
        super(message);
    }
}
