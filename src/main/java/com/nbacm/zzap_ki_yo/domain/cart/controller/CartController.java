package com.nbacm.zzap_ki_yo.domain.cart.controller;

import com.nbacm.zzap_ki_yo.domain.cart.dto.AddToCartRequestDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.CartResponseDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.UpdateCartItemRequestDto;
import com.nbacm.zzap_ki_yo.domain.cart.service.CartRedisServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartRedisServiceImpl cartRedisService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addToCart(@Auth AuthUser authUser, @Valid @RequestBody AddToCartRequestDto requestDto) {
        CartResponseDto response = cartRedisService.addToCart(authUser.getEmail(), requestDto);
        log.info("response: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@Auth AuthUser authUser) {
        CartResponseDto response = cartRedisService.getCart(authUser.getEmail());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponseDto> updateCartItem(
            @Auth AuthUser authUser,
            @Valid @RequestBody UpdateCartItemRequestDto requestDto) {
        CartResponseDto response = cartRedisService.updateCartItem(authUser.getEmail(), requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@Auth AuthUser authUser) {
        cartRedisService.clearCart(authUser.getEmail());
        return ResponseEntity.noContent().build();
    }

}
