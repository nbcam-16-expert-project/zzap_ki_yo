package com.nbacm.zzap_ki_yo.domain.review.exception;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException(String message) { super(message); }
}
