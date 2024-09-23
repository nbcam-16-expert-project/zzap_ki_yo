package com.nbacm.zzap_ki_yo.domain.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.cart.dto.AddToCartRequestDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.CartItemResponseDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.CartResponseDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.UpdateCartItemRequestDto;
import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.exception.MenuNotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartRedisServiceImpl implements CartRedisService {
    private final RedisTemplate<String,Object> redisObjectTemplate;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper;
    private static final String CART_KEY_PREFIX = "cart:";
    private void saveCart(String userEmail, CartResponseDto cartDto) {
        String key = CART_KEY_PREFIX + userEmail;
        try {
            ObjectMapper mapper = new ObjectMapper();
            // CartResponseDto를 JSON 문자열로 직렬화
            String jsonValue = mapper.writeValueAsString(cartDto);
            // JSON 문자열을 Redis에 저장
            redisObjectTemplate.opsForValue().set(key, jsonValue, 24, TimeUnit.HOURS); // 24시간 후 만료
        } catch (JsonProcessingException e) {
            log.error("CartResponseDto 직렬화 중 오류 발생", e);
        }
    }
    private CartResponseDto getCartInternal(String userEmail) {
        String key = CART_KEY_PREFIX + userEmail;
        // Redis에서 저장된 데이터를 가져옴
        String jsonValue = (String) redisObjectTemplate.opsForValue().get(key);

        if (jsonValue != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                // JSON 문자열을 CartResponseDto 객체로 변환 (역직렬화)
                return mapper.readValue(jsonValue, CartResponseDto.class);
            } catch (JsonProcessingException e) {
                log.error("CartResponseDto 역직렬화 중 오류 발생", e);
            }
        }

        return null;
    }
    @Override
    @Transactional
    public CartResponseDto addToCart(String userEmail, AddToCartRequestDto requestDto) {
        log.debug("Starting addToCart method: userEmail={}, requestDto={}", userEmail, requestDto);
        log.info("Adding to cart: userEmail={}, menuId={}, quantity={}", userEmail, requestDto.getMenuId(), requestDto.getQuantity());

        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

        Store store = menu.getStore();
        if (store.getStoreType() == StoreType.CLOSING) {
            throw new UnauthorizedException("폐업한 가게의 메뉴는 장바구니에 담을 수 없습니다.");
        }

        CartResponseDto cart = getCartInternal(userEmail);
        if (cart == null || (cart.getStoreId() != null && !cart.getStoreId().equals(store.getStoreId()))) {
            cart = CartResponseDto.builder()
                    .userEmail(userEmail)
                    .storeId(store.getStoreId())
                    .storeName(store.getStoreName())
                    .items(new ArrayList<>())
                    .build();
        }

        CartItemResponseDto newItem = CartItemResponseDto.builder()
                .menuId(menu.getMenuId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .quantity(requestDto.getQuantity())
                .build();

        List<CartItemResponseDto> updatedItems = cart.getItems().stream()
                .map(item -> {
                    if (item.getMenuId().equals(newItem.getMenuId())) {
                        return CartItemResponseDto.builder()
                                .menuId(item.getMenuId())
                                .menuName(item.getMenuName())
                                .price(item.getPrice())
                                .quantity(item.getQuantity() + newItem.getQuantity())
                                .build();
                    }
                    return item;
                })
                .collect(Collectors.toList());

        if (updatedItems.size() == cart.getItems().size()) {
            updatedItems.add(newItem);
        }

        CartResponseDto updatedCart = CartResponseDto.builder()
                .userEmail(cart.getUserEmail())
                .storeId(cart.getStoreId())
                .storeName(cart.getStoreName())
                .items(updatedItems)
                .build();

        saveCart(userEmail, updatedCart);
        log.debug("Finished addToCart method successfully");
        return updatedCart;
    }

    @Override
    public CartResponseDto getCart(String userEmail) {
        log.info("Fetching cart for user: {}", userEmail);
        return getCartInternal(userEmail);
    }

    @Override
    @Transactional
    public CartResponseDto updateCartItem(String userEmail, UpdateCartItemRequestDto requestDto) {
        log.info("Updating cart item: userEmail={}, menuId={}, quantity={}", userEmail, requestDto.getMenuId(), requestDto.getQuantity());

        CartResponseDto cart = getCartInternal(userEmail);
        if (cart == null) {
            throw new StoreNotFoundException("장바구니를 찾을 수 없습니다.");
        }

        List<CartItemResponseDto> updatedItems = new ArrayList<>();
        boolean itemFound = false;

        for (CartItemResponseDto item : cart.getItems()) {
            if (item.getMenuId().equals(requestDto.getMenuId())) {
                // 기존 아이템 업데이트
                updatedItems.add(CartItemResponseDto.builder()
                        .menuId(item.getMenuId())
                        .menuName(item.getMenuName())
                        .price(item.getPrice())
                        .quantity(requestDto.getQuantity())
                        .build());
                itemFound = true;
            } else {
                // 다른 아이템은 그대로 유지
                updatedItems.add(item);
            }
        }

        if (!itemFound) {
            // 새로운 메뉴 아이템 추가 로직
            Menu menu = menuRepository.findById(requestDto.getMenuId())
                    .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

            // 장바구니의 가게 ID와 메뉴의 가게 ID가 일치하는지 확인
            if (!cart.getStoreId().equals(menu.getStore().getStoreId())) {
                throw new IllegalArgumentException("다른 가게의 메뉴를 추가할 수 없습니다.");
            }

            updatedItems.add(CartItemResponseDto.builder()
                    .menuId(menu.getMenuId())
                    .menuName(menu.getMenuName())
                    .price(menu.getPrice())
                    .quantity(requestDto.getQuantity())
                    .build());
        }

        // 수량이 0인 아이템 제거
        updatedItems = updatedItems.stream()
                .filter(item -> item.getQuantity() > 0)
                .collect(Collectors.toList());

        CartResponseDto updatedCart = CartResponseDto.builder()
                .userEmail(cart.getUserEmail())
                .storeId(cart.getStoreId())
                .storeName(cart.getStoreName())
                .items(updatedItems)
                .build();

        saveCart(userEmail, updatedCart);
        log.info("Updated cart item successfully: userEmail={}, menuId={}", userEmail, requestDto.getMenuId());
        return updatedCart;
    }

    @Override
    @Transactional
    public void clearCart(String userEmail) {
        log.info("Clearing cart for user: {}", userEmail);
        String key = CART_KEY_PREFIX + userEmail;
        redisObjectTemplate.delete(key);
        log.info("Cleared cart successfully: userEmail={}", userEmail);
    }

    @Override
    @Transactional
    public CartResponseDto removeCartItem(String userEmail, Long menuId) {
        log.info("Removing item from cart: userEmail={}, menuId={}", userEmail, menuId);

        CartResponseDto cart = getCartInternal(userEmail);
        if (cart == null) {
            throw new StoreNotFoundException("장바구니를 찾을 수 없습니다.");
        }

        List<CartItemResponseDto> updatedItems = cart.getItems().stream()
                .filter(item -> !item.getMenuId().equals(menuId))
                .collect(Collectors.toList());

        CartResponseDto updatedCart = CartResponseDto.builder()
                .userEmail(cart.getUserEmail())
                .storeId(cart.getStoreId())
                .storeName(cart.getStoreName())
                .items(updatedItems)
                .build();

        saveCart(userEmail, updatedCart);
        log.info("Removed item from cart successfully: userEmail={}, menuId={}", userEmail, menuId);
        return updatedCart;
    }
}
