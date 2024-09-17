package com.nbacm.zzap_ki_yo.domain.user.common.util;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;

    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/api/v1/users/login",
            "/api/v1/users"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (isPublicUrl(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(JwtUtils.AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(JwtUtils.BEARER_PREFIX)) {
            String token = authHeader.substring(JwtUtils.BEARER_PREFIX.length());
            try {
                if (jwtUtils.validateToken(token)) {
                    String email = jwtUtils.getUserEmailFromToken(token);
                    String role = jwtUtils.getUserRoleFromToken(token);
                    // RefreshToken 존재 여부 확인
                    String refreshToken = redisTemplate.opsForValue().get("RT:" + email);
                    if (refreshToken == null) {
                        throw new JwtException("로그아웃된 사용자입니다.");
                    }
                    // 기본적인 역할 검사
                    if (isAdminOnlyEndpoint(path) && !"ADMIN".equals(role)) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("관리자 권한이 필요합니다.");
                        return;
                    }

                    request.setAttribute("AuthenticatedUser", email);
                    request.setAttribute("UserRole", role);
                } else {
                    throw new JwtException("유효하지 않거나 이미 만료된 토큰입니다.");
                }
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT 인증 실패: " + e.getMessage());
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("인증 토큰이 필요합니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicUrl(String path) {
        return PUBLIC_URLS.stream().anyMatch(path::startsWith);
    }

    private boolean isAdminOnlyEndpoint(String path) {
        // ADMIN 전용 엔드포인트 패턴을 정의
        return path.contains("/admin/") ||
                (path.contains("/users") && (path.endsWith("/all") || path.contains("/manage"))) ||
                (path.contains("/orders") && path.endsWith("/all")) ||
                (path.contains("/stores") && (path.contains("/create") || path.contains("/update") || path.contains("/delete"))) ||
                (path.contains("/menu") && (path.contains("/create") || path.contains("/update") || path.contains("/delete"))) ||
                (path.contains("/reviews") && path.contains("/manage")) ||
                (path.contains("/payments") && path.endsWith("/all"));
    }
}