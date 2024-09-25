package com.nbacm.zzap_ki_yo.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.user.common.util.PasswordUtil;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PasswordUtil passwordUtil;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    public String getKakaoAccessToken(String code) {
        log.info("Attempting to get Kakao access token with code: {}", code);

        String accessTokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(accessTokenUrl, request, String.class);
            log.info("Kakao API response status: {}", response.getStatusCode());
            log.debug("Kakao API response body: {}", response.getBody());

            JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (HttpClientErrorException e) {
            log.error("HTTP error when getting Kakao access token. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to get Kakao access token", e);
        } catch (Exception e) {
            log.error("Error getting Kakao access token", e);
            throw new RuntimeException("Failed to get Kakao access token", e);
        }
    }

    public User getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Kakao user info response", e);
        }

        String kakaoId = jsonNode.get("id").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        return userRepository.findByKakaoId(kakaoId)
                .map(existingUser -> {
                    // 기존 사용자의 경우, 필요하다면 정보를 업데이트할 수 있습니다.
                    // 예: existingUser.updateKakaoInfo(nickname);
                    return existingUser;
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .nickname(nickname)
                            .name(nickname)
                            .userRole(UserRole.USER)
                            .password(passwordUtil.encode("kakaoUser")) // 임시 비밀번호
                            .kakaoId(kakaoId) // 여기서 kakaoId를 설정합니다.
                            .build();
                    return userRepository.save(newUser);
                });
    }
}