package com.nbacm.zzap_ki_yo.domain.user.service;

import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.common.util.PasswordUtil;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserRequestDto;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserResponseDto;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.exception.AlreadyExistsException;
import com.nbacm.zzap_ki_yo.domain.user.exception.InvalidRoleException;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import com.nbacm.zzap_ki_yo.domain.user.service.KakaoService;
import com.nbacm.zzap_ki_yo.domain.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // JUnit5와 Mockito 통합을 사용하려면 추가해야 함

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private KakaoService kakaoService;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private UserServiceImpl userService;


    private UserRequestDto userRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto("test@email.com", "Test Name", "TestNickname", "password123!", UserRole.USER);
        user = User.builder()
                .email(userRequestDto.getEmail())
                .name(userRequestDto.getName())
                .nickname(userRequestDto.getNickname())
                .password("encodedPassword")
                .userRole(UserRole.USER)
                .build();
    }

    @Test
    void signUpSuccess() {
        when(passwordUtil.encode(anyString())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .email(userRequestDto.getEmail())
                .password("encodedPassword")
                .name(userRequestDto.getName())
                .nickname(userRequestDto.getNickname())
                .userRole(UserRole.USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDto result = userService.signUp(userRequestDto);

        assertNotNull(result);
        assertEquals(userRequestDto.getEmail(), result.getEmail());
        assertEquals(userRequestDto.getName(), result.getName());
        assertEquals(userRequestDto.getNickname(), result.getNickname());
        assertEquals(UserRole.USER, result.getUserRole());

        verify(userRepository).save(any(User.class));
        verify(passwordUtil).encode(userRequestDto.getPassword());
    }

    @Test
    void loginSuccess() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(passwordUtil.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(anyString(), any(UserRole.class))).thenReturn("accessToken");
        when(jwtUtils.generateRefreshToken(anyString(), any(UserRole.class))).thenReturn("refreshToken");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        String result = userService.login(userRequestDto);

        assertNotNull(result);
        assertEquals("accessToken", result);

        verify(redisTemplate.opsForValue()).set(eq("RT:test@email.com"), eq("refreshToken"), anyLong(), any());
    }

    @Test
    void loginFailWrongPassword() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(passwordUtil.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.login(userRequestDto));
    }

    @Test
    void loginFailDeletedAccount() {
        User deletedUser = User.builder()
                .email(userRequestDto.getEmail())
                .name(userRequestDto.getName())
                .nickname(userRequestDto.getNickname())
                .password("encodedPassword")
                .userRole(UserRole.USER)
                .build();
        deletedUser.deleteAccount();

        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(deletedUser);
        when(passwordUtil.matches(anyString(), anyString())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.login(userRequestDto));
    }

    @Test
    void updateUserSuccess() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(passwordUtil.encode(anyString())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto result = userService.updateUser("test@email.com", userRequestDto);

        assertNotNull(result);
        assertEquals(userRequestDto.getNickname(), result.getNickname());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateAdminSuccess() {
        User adminUser = User.builder()
                .email("admin@email.com")
                .name("Admin")
                .nickname("AdminNick")
                .password("adminPassword")
                .userRole(UserRole.ADMIN)
                .build();

        when(userRepository.findByEmailOrElseThrow("admin@email.com")).thenReturn(adminUser);
        when(userRepository.findByEmailOrElseThrow("test@email.com")).thenReturn(user);
        when(passwordUtil.encode(anyString())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto result = userService.updateAdmin("admin@email.com", "test@email.com", userRequestDto);

        assertNotNull(result);
        assertEquals(userRequestDto.getNickname(), result.getNickname());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateAdminFailNotAdmin() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);

        assertThrows(InvalidRoleException.class, () -> userService.updateAdmin("test@email.com", "target@email.com", userRequestDto));
    }

    @Test
    void deleteUserSuccess() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(passwordUtil.matches(anyString(), anyString())).thenReturn(true);
        when(redisTemplate.delete(anyString())).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser("test@email.com", "password123!"));

        verify(userRepository).save(any(User.class));
        verify(redisTemplate).delete("RT:test@email.com");
    }

    @Test
    void deleteUserFailWrongPassword() {
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(passwordUtil.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser("test@email.com", "wrongPassword"));
    }
    @Test
    void kakaoLoginSuccess() {
        // Given
        String code = "testCode";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        // KakaoService에서 사용하는 메서드 모킹
        when(kakaoService.getKakaoAccessToken(code)).thenReturn(accessToken);
        when(kakaoService.getKakaoUserInfo(accessToken)).thenReturn(user); // Kakao에서 받아온 사용자 정보

        // JWT 관련 메서드 모킹
        when(jwtUtils.generateToken(anyString(), any(UserRole.class))).thenReturn("jwtToken");
        when(jwtUtils.generateRefreshToken(anyString(), any(UserRole.class))).thenReturn(refreshToken);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // When
        String result = userService.kakaoLogin(code);

        // Then
        assertNotNull(result);
        assertEquals("jwtToken", result);

        // KakaoService가 호출되었는지 검증
        verify(kakaoService).getKakaoAccessToken(code);
        verify(kakaoService).getKakaoUserInfo(accessToken);

        // JWT 토큰 생성 및 Redis에 저장 확인
        verify(jwtUtils).generateToken(user.getEmail(), user.getUserRole());
        verify(jwtUtils).generateRefreshToken(user.getEmail(), user.getUserRole());
        verify(redisTemplate.opsForValue()).set(
                eq("RT:" + user.getEmail()),
                eq(refreshToken),
                anyLong(),
                eq(TimeUnit.MILLISECONDS)
        );
    }
    @Test
    void logoutSuccess() {
        // Given
        String accessToken = "accessToken";
        String email = "test@email.com";

        // JWT 관련 모킹
        when(jwtUtils.getUserEmailFromToken(accessToken)).thenReturn(email);

        // JWT의 expireAccessToken이 void가 아닐 경우 처리
        when(jwtUtils.expireAccessToken(accessToken)).thenReturn(null); // 반환 타입에 맞게 설정 (만약 반환 값이 있다면)

        // Redis 관련 모킹
        when(redisTemplate.delete("RT:" + email)).thenReturn(true);

        // When
        String result = userService.logout(accessToken);

        // Then
        assertNull(result);

        // JWT와 Redis 관련 호출 검증
        verify(jwtUtils).expireAccessToken(accessToken);
        verify(jwtUtils).getUserEmailFromToken(accessToken);
        verify(redisTemplate).delete("RT:" + email);
    }
}
