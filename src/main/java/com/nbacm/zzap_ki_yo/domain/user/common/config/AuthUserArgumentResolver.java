package com.nbacm.zzap_ki_yo.domain.user.common.config;

import com.nbacm.zzap_ki_yo.domain.user.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);

    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String email = (String) webRequest.getAttribute("AuthenticatedUser", NativeWebRequest.SCOPE_REQUEST);
        UserRole role = (UserRole) webRequest.getAttribute("UserRole", NativeWebRequest.SCOPE_REQUEST);
        return new AuthUser(email, role);
    }
}
