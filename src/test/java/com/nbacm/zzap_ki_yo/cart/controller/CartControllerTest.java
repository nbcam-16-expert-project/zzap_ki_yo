package com.nbacm.zzap_ki_yo.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.cart.controller.CartController;
import com.nbacm.zzap_ki_yo.domain.cart.dto.AddToCartRequestDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.CartItemResponseDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.CartResponseDto;
import com.nbacm.zzap_ki_yo.domain.cart.dto.UpdateCartItemRequestDto;
import com.nbacm.zzap_ki_yo.domain.cart.service.CartRedisServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;


public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private CartRedisServiceImpl cartRedisService;

    @InjectMocks
    private CartController cartController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private CartResponseDto cartResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().equals(AuthUser.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return new AuthUser("test@example.com", UserRole.USER);
                    }
                })
                .build();

        cartResponseDto = CartResponseDto.builder()
                .userEmail("test@example.com")
                .storeId(1L)
                .storeName("Test Store")
                .items(Arrays.asList(
                        new CartItemResponseDto(1L, "Test Menu", 1000, 2)
                ))
                .totalPrice(2000)
                .build();
    }

    @Test
    void testAddToCart() throws Exception {
        AddToCartRequestDto requestDto = new AddToCartRequestDto(1L, 2);

        when(cartRedisService.addToCart(eq("test@example.com"), any(AddToCartRequestDto.class)))
                .thenReturn(cartResponseDto);

        mockMvc.perform(post("/api/v1/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPrice").value(2000))
                .andExpect(jsonPath("$.items[0].menuId").value(1))
                .andExpect(jsonPath("$.items[0].quantity").value(2));

        verify(cartRedisService).addToCart(eq("test@example.com"), any(AddToCartRequestDto.class));
    }

    @Test
    void testGetCart() throws Exception {
        when(cartRedisService.getCart("test@example.com")).thenReturn(cartResponseDto);

        mockMvc.perform(get("/api/v1/cart")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPrice").value(2000))
                .andExpect(jsonPath("$.items[0].menuId").value(1))
                .andExpect(jsonPath("$.items[0].quantity").value(2));

        verify(cartRedisService).getCart("test@example.com");
    }

    @Test
    void testUpdateCartItem() throws Exception {
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(1L, 3);

        when(cartRedisService.updateCartItem(eq("test@example.com"), any(UpdateCartItemRequestDto.class)))
                .thenReturn(cartResponseDto);

        mockMvc.perform(put("/api/v1/cart/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPrice").value(2000))
                .andExpect(jsonPath("$.items[0].menuId").value(1))
                .andExpect(jsonPath("$.items[0].quantity").value(2));

        verify(cartRedisService).updateCartItem(eq("test@example.com"), any(UpdateCartItemRequestDto.class));
    }

    @Test
    void testClearCart() throws Exception {
        doNothing().when(cartRedisService).clearCart("test@example.com");

        mockMvc.perform(delete("/api/v1/cart/clear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cartRedisService).clearCart("test@example.com");
    }

    @Test
    void testAddToCartWithInvalidRequest() throws Exception {
        AddToCartRequestDto invalidRequestDto = new AddToCartRequestDto(null, 0);

        mockMvc.perform(post("/api/v1/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(cartRedisService, never()).addToCart(any(), any());
    }

    @Test
    void testUpdateCartItemWithInvalidRequest() throws Exception {
        UpdateCartItemRequestDto invalidRequestDto = new UpdateCartItemRequestDto(null, -1);

        mockMvc.perform(put("/api/v1/cart/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(cartRedisService, never()).updateCartItem(any(), any());
    }
}
