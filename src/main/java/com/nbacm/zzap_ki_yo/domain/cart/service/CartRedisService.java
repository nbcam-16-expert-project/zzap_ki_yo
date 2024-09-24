package com.nbacm.zzap_ki_yo.domain.cart.service;

import com.nbacm.zzap_ki_yo.domain.cart.dto.AddToCartRequestDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.CartResponseDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.UpdateCartItemRequestDto;

public interface CartRedisService {
    CartResponseDto addToCart(String userEmail, AddToCartRequestDto requestDto);
    CartResponseDto getCart(String userEmail);
    CartResponseDto updateCartItem(String userEmail, UpdateCartItemRequestDto requestDto);
    void clearCart(String userEmail);
    CartResponseDto removeCartItem(String userEmail, Long menuId);
}
