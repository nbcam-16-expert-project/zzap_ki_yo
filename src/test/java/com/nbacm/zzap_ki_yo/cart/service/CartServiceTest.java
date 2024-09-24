package com.nbacm.zzap_ki_yo.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.cart.dto.AddToCartRequestDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.CartItemResponseDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.CartResponseDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.UpdateCartItemRequestDto;
import com.nbacm.zzap_ki_yo.domain.cart.service.CartRedisServiceImpl;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.exception.MenuNotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class CartServiceTest {
    @Mock
    private RedisTemplate<String, Object> objectRedisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations; // 모킹할 ValueOperations


    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CartRedisServiceImpl cartRedisServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // opsForValue() 호출 시, 모킹된 ValueOperations를 반환
        when(objectRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }
    @Test
    void addToCart_Success() throws JsonProcessingException {
        // given
        String userEmail = "test@example.com";
        Long menuId = 1L;
        int quantity = 2;

        AddToCartRequestDto requestDto = new AddToCartRequestDto(menuId, quantity);
        Menu menu = mock(Menu.class);
        Store store = mock(Store.class);

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(menu.getStore()).thenReturn(store);
        when(store.getStoreType()).thenReturn(StoreType.OPENING);
        when(store.getStoreId()).thenReturn(1L);
        when(store.getStoreName()).thenReturn("Test Store");
        when(menu.getMenuId()).thenReturn(menuId);
        when(menu.getMenuName()).thenReturn("Pizza");
        when(menu.getPrice()).thenReturn(10000);

        // when
        CartResponseDto result = cartRedisServiceImpl.addToCart(userEmail, requestDto);

        // then
        assertEquals(userEmail, result.getUserEmail());
        assertEquals(store.getStoreId(), result.getStoreId());
        assertEquals(1, result.getItems().size());
        assertEquals(menuId, result.getItems().get(0).getMenuId());
        assertEquals(10000, result.getItems().get(0).getPrice());
        assertEquals(quantity, result.getItems().get(0).getQuantity());

        // Redis 저장 로직 확인
        verify(objectRedisTemplate.opsForValue(), times(1)).set(
                eq("cart:" + userEmail),
                anyString(),
                eq(24L),
                eq(TimeUnit.HOURS)
        );
    }

    @Test
    void addToCart_MenuNotFound() {
        // given
        String userEmail = "test@example.com";
        Long menuId = 1L;
        int quantity = 2;

        AddToCartRequestDto requestDto = new AddToCartRequestDto(menuId, quantity);

        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MenuNotFoundException.class, () -> cartRedisServiceImpl.addToCart(userEmail, requestDto));
    }

    @Test
    void getCart_ReturnsExistingCart() throws JsonProcessingException {
        // given
        String userEmail = "test@example.com";
        String redisKey = "cart:" + userEmail;

        CartResponseDto cart = CartResponseDto.builder()
                .userEmail(userEmail)
                .storeId(1L)
                .storeName("Test Store")
                .items(new ArrayList<>())
                .build();

        String cartJson = "{\"userEmail\":\"test@example.com\",\"storeId\":1,\"storeName\":\"Test Store\",\"items\":[]}";
        when(objectRedisTemplate.opsForValue().get(redisKey)).thenReturn(cartJson);
        when(objectMapper.readValue(cartJson, CartResponseDto.class)).thenReturn(cart);

        // when
        CartResponseDto result = cartRedisServiceImpl.getCart(userEmail);

        // then
        assertNotNull(result);
        assertEquals(userEmail, result.getUserEmail());
        assertEquals(1L, result.getStoreId());
        assertEquals("Test Store", result.getStoreName());
    }

    @Test
    void getCart_ReturnsNull_WhenNoCartInRedis() {
        // given
        String userEmail = "test@example.com";
        String redisKey = "cart:" + userEmail;

        when(objectRedisTemplate.opsForValue().get(redisKey)).thenReturn(null);

        // when
        CartResponseDto result = cartRedisServiceImpl.getCart(userEmail);

        // then
        assertNull(result);
    }

    @Test
    void updateCartItem_Success() throws JsonProcessingException {
        // given
        String userEmail = "test@example.com";
        Long menuId = 1L;
        int newQuantity = 3;

        CartItemResponseDto cartItem = CartItemResponseDto.builder()
                .menuId(menuId)
                .menuName("Pizza")
                .price(10000)
                .quantity(2)
                .build();

        CartResponseDto cart = CartResponseDto.builder()
                .userEmail(userEmail)
                .storeId(1L)
                .storeName("Test Store")
                .items(List.of(cartItem))
                .build();

        String cartJson = "{\"userEmail\":\"test@example.com\",\"storeId\":1,\"storeName\":\"Test Store\",\"items\":[{\"menuId\":1,\"menuName\":\"Pizza\",\"price\":10000,\"quantity\":2}]}";

        // opsForValue().get() 모킹
        when(objectRedisTemplate.opsForValue().get("cart:" + userEmail)).thenReturn(cartJson);

        // ObjectMapper로 CartResponseDto 변환 모킹
        when(objectMapper.readValue(cartJson, CartResponseDto.class)).thenReturn(cart);

        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(menuId, newQuantity);

        // when
        CartResponseDto result = cartRedisServiceImpl.updateCartItem(userEmail, requestDto);

        // then
        assertEquals(newQuantity, result.getItems().get(0).getQuantity());

        // opsForValue().set() 모킹 확인 (저장)
        verify(objectRedisTemplate.opsForValue(), times(1)).set(
                eq("cart:" + userEmail),
                anyString(),
                eq(24L),
                eq(TimeUnit.HOURS)
        );

        // opsForValue().get() 호출 검증
        verify(objectRedisTemplate.opsForValue(), times(1)).get("cart:" + userEmail);
    }

    @Test
    void removeCartItem_Success() throws JsonProcessingException {
        // given
        String userEmail = "test@example.com";
        Long menuId = 1L;

        CartItemResponseDto cartItem = CartItemResponseDto.builder()
                .menuId(menuId)
                .menuName("Pizza")
                .price(10000)
                .quantity(2)
                .build();

        CartResponseDto cart = CartResponseDto.builder()
                .userEmail(userEmail)
                .storeId(1L)
                .storeName("Test Store")
                .items(List.of(cartItem))
                .build();

        String cartJson = "{\"userEmail\":\"test@example.com\",\"storeId\":1,\"storeName\":\"Test Store\",\"items\":[{\"menuId\":1,\"menuName\":\"Pizza\",\"price\":10000,\"quantity\":2}]}";

        // opsForValue().get() 호출에 대한 모킹
        when(objectRedisTemplate.opsForValue().get("cart:" + userEmail)).thenReturn(cartJson);

        // ObjectMapper로 CartResponseDto 변환에 대한 모킹
        when(objectMapper.readValue(cartJson, CartResponseDto.class)).thenReturn(cart);

        // when
        CartResponseDto result = cartRedisServiceImpl.removeCartItem(userEmail, menuId);

        // then
        assertTrue(result.getItems().isEmpty());

        // opsForValue().set() 호출에 대한 검증
        verify(objectRedisTemplate.opsForValue(), times(1)).set(
                eq("cart:" + userEmail),
                anyString(),
                eq(24L),
                eq(TimeUnit.HOURS)
        );

        // opsForValue().get() 호출에 대한 검증
        verify(objectRedisTemplate.opsForValue(), times(1)).get("cart:" + userEmail);
    }

    @Test
    void clearCart_Success() {
        // given
        String userEmail = "test@example.com";
        String redisKey = "cart:" + userEmail;

        // when
        cartRedisServiceImpl.clearCart(userEmail);

        // then
        verify(objectRedisTemplate, times(1)).delete(redisKey);
    }
}
