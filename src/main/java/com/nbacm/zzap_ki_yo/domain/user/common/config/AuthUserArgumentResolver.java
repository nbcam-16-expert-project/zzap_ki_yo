package com.nbacm.zzap_ki_yo.domain.user.common.config;

import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
@Slf4j
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    // @Auth 어노테이션이 있는지 확인
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Auth.class) != null;
    }

    // AuthUser 객체를 생성하여 반환
    @Override
    public Object resolveArgument(
            @Nullable MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String email = (String) request.getAttribute("AuthenticatedUser");
        UserRole role= (UserRole) request.getAttribute("role");
        log.info("Resolving AuthUser - Email: {}, Role: {}", email, role);

        return new AuthUser(email, role);
    }
}