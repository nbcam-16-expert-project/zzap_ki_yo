package com.nbacm.zzap_ki_yo.domain.user.common.util;

import com.nbacm.zzap_ki_yo.domain.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@Getter
public class JwtUtils {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 15 * 60 * 100000L; // 15분

    private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 14일

    // Base64 Encode 한 SecretKey
    @Value("${jwt.secret.key}")
    private  String SECRET_KEY;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());

    }

    // JWT 토큰 생성
    public String generateToken(String email, UserRole role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY,role.name())
                .claim("email", email)  // 이메일도 명시적으로 저장
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }
    //Refresh Token 생성
    public String generateRefreshToken(String email,UserRole role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, role.name())  // role을 문자열로 저장
                .claim("email", email)  // 이메일도 명시적으로 저장
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    // AccessToken을 즉시 만료시키는 메서드
    public String expireAccessToken(String token) {
        Claims claims = parseClaims(token);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(now) // 현재 시간으로 만료 설정
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    // 리프레시 토큰의 만료 시간 반환
    public Long getRefreshTokenExpirationTime() {
        return REFRESH_TOKEN_TIME;
    }


    // 토큰에서 사용자 이름 추출
    public String getUserEmailFromToken(String token) {
        Claims claims = parseClaims(token);
        String email = claims.getSubject();
        logger.info("JwtUtils - Extracted email from token: {}", email);
        return email;
    }

    public String getUserRoleFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get(AUTHORIZATION_KEY, String.class);
    }


    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        }
        catch (Exception e) {
            logger.warn("이미 만료된 토큰 입니다",e);
            return true;
        }
    }
    public String expireToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(now) // 현재 시간으로 만료 설정
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    // JWT 토큰에서 Claims 추출
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .setAllowedClockSkewSeconds(60)
                .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
                .getBody();
    }
}
