package com.nbacm.zzap_ki_yo.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.controller.UserController;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserRequestDto;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserResponseDto;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.service.KakaoService;
import com.nbacm.zzap_ki_yo.domain.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserController userController;

    @MockBean
    private KakaoService kakaoService;


    private MockMvc mockMvc;


    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    void login_success() throws Exception {
        // Given
        UserRequestDto requestDto = new UserRequestDto("test@email.com", null, null, "password123!", UserRole.USER);
        String token = "jwt-token";

        when(userService.login(any(UserRequestDto.class))).thenReturn(token);

        // When & Then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }
    @Test
    void logout_success() throws Exception {
        // Given
        String token = "Bearer jwt-token";
        String expiredToken = "expired-jwt-token";

        when(userService.logout(any(String.class))).thenReturn(expiredToken);

        // When & Then
        mockMvc.perform(post("/api/v1/users/logout")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string(expiredToken));
    }
    @Test
    void update_success() throws Exception {
        // Given
        UserRequestDto requestDto = new UserRequestDto("test@email.com", "Updated Name", "UpdatedNickname", "password123!", UserRole.USER);
        UserResponseDto responseDto = new UserResponseDto("Updated Name", "test@email.com", "UpdatedNickname", UserRole.USER);

        // 모의 객체 설정
        when(userService.updateUser(eq("test@email.com"), any(UserRequestDto.class))).thenReturn(responseDto);

        // Mock the AuthUser
        HandlerMethodArgumentResolver authUserResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType().equals(AuthUser.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return new AuthUser("test@email.com", UserRole.USER);  // 모킹된 AuthUser 반환
            }
        };

        // MockMvc에 ArgumentResolver 추가
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(authUserResolver)  // 커스텀 리졸버 추가
                .build();

        // When & Then
        mockMvc.perform(put("/api/v1/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("Authorization", "Bearer jwt-token"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void deleteAccount_success() throws Exception {
        // Given
        UserRequestDto requestDto = new UserRequestDto("test@email.com", null, null, "password123!", UserRole.USER);

        // Mock the AuthUser
        HandlerMethodArgumentResolver authUserResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType().equals(AuthUser.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return new AuthUser("test@email.com", UserRole.USER);  // 모킹된 AuthUser 반환
            }
        };

        // Mock the deleteUser service call
        doNothing().when(userService).deleteUser(eq("test@email.com"), eq("password123!"));

        // MockMvc에 ArgumentResolver 추가
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(authUserResolver)  // 커스텀 리졸버 추가
                .build();

        // When & Then
        mockMvc.perform(delete("/api/v1/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("Authorization", "Bearer jwt-token"))
                .andExpect(status().isNoContent());  // 204 No Content 확인
    }
}
