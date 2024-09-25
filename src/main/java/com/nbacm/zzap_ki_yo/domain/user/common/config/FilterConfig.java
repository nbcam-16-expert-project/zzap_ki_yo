package com.nbacm.zzap_ki_yo.domain.user.common.config;

import com.nbacm.zzap_ki_yo.domain.user.common.util.*;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;

    // Filter 등록
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtUtils, redisTemplate));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
