package com.nbacm.zzap_ki_yo.domain.user.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import com.nbacm.zzap_ki_yo.domain.user.common.util.PasswordUtil;
import com.nbacm.zzap_ki_yo.domain.user.service.KakaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class KakaoServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private KakaoService kakaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetKakaoAccessToken() throws Exception {
        // Given
        String code = "testCode";
        String accessToken = "testAccessToken";
        String accessTokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "testClientId");
        params.add("client_secret", "testClientSecret");
        params.add("redirect_uri", "testRedirectUri");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // Mock ResponseEntity to be returned by RestTemplate
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"access_token\":\"" + accessToken + "\"}", HttpStatus.OK);

        // Set up the mock interaction for restTemplate
        when(restTemplate.postForEntity(eq(accessTokenUrl), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        // Mock ObjectMapper to parse the response body
        JsonNode jsonNode = new ObjectMapper().readTree("{\"access_token\":\"" + accessToken + "\"}");
        when(objectMapper.readTree(anyString())).thenReturn(jsonNode);

        // When
        String resultToken = kakaoService.getKakaoAccessToken(code);

        // Then
        assertEquals(accessToken, resultToken);
        verify(restTemplate, times(1)).postForEntity(eq(accessTokenUrl), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testGetKakaoUserInfo_NewUser() throws Exception {
        // Given
        String accessToken = "testAccessToken";
        String kakaoId = "123456789";
        String email = "test@kakao.com";
        String nickname = "testNickname";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        String kakaoApiResponse = "{ \"id\": \"" + kakaoId + "\", \"kakao_account\": { \"email\": \"" + email + "\" }, \"properties\": { \"nickname\": \"" + nickname + "\" } }";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(kakaoApiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        )).thenReturn(responseEntity);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseEntity.getBody());
        when(objectMapper.readTree(anyString())).thenReturn(jsonNode);

        when(userRepository.findByKakaoId(kakaoId)).thenReturn(Optional.empty());
        when(passwordUtil.encode(anyString())).thenReturn("encodedPassword");

        User newUser = User.builder()
                .email(email)
                .nickname(nickname)
                .name(nickname)
                .userRole(UserRole.USER)
                .password("encodedPassword")
                .kakaoId(kakaoId)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // When
        User createdUser = kakaoService.getKakaoUserInfo(accessToken);

        // Then
        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals(nickname, createdUser.getNickname());
        assertEquals(kakaoId, createdUser.getKakaoId());
        assertEquals(UserRole.USER, createdUser.getUserRole());
        verify(userRepository, times(1)).findByKakaoId(kakaoId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetKakaoUserInfo_ExistingUser() throws Exception {
        // Given
        String accessToken = "testAccessToken";
        String kakaoId = "123456789";
        String email = "test@kakao.com";
        String nickname = "testNickname";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Object> kakaoUserInfoRequest = new HttpEntity<>(headers);

        String kakaoApiResponse = "{ \"id\": \"" + kakaoId + "\", \"kakao_account\": { \"email\": \"" + email + "\" }, \"properties\": { \"nickname\": \"" + nickname + "\" } }";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(kakaoApiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        )).thenReturn(responseEntity);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseEntity.getBody());
        when(objectMapper.readTree(anyString())).thenReturn(jsonNode);

        User existingUser = User.builder()
                .email(email)
                .nickname(nickname)
                .name(nickname)
                .userRole(UserRole.USER)
                .password("encodedPassword")
                .kakaoId(kakaoId)
                .build();
        when(userRepository.findByKakaoId(kakaoId)).thenReturn(Optional.of(existingUser));

        // When
        User foundUser = kakaoService.getKakaoUserInfo(accessToken);

        // Then
        assertNotNull(foundUser);
        assertEquals(existingUser, foundUser);
        verify(userRepository, times(1)).findByKakaoId(kakaoId);
        verify(userRepository, never()).save(any(User.class));  // 기존 사용자는 저장하지 않음
    }
}
