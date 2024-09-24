package com.nbacm.zzap_ki_yo.domain.favorite.exception;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;

public class FavoriteNotFoundException extends NotFoundException {
    public FavoriteNotFoundException(String message) { super(message); }
}
