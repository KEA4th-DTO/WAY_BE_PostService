package com.dto.way.post.global.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 현재 요청의 컨텍스트를 가져옵니다.
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            // 현재 HTTP 요청을 가져옵니다.
            HttpServletRequest request = attributes.getRequest();

            // HTTP 요청의 "Authorization" 헤더에서 토큰을 가져옵니다.
            String token = request.getHeader("Authorization");

            // Feign 요청에 "Authorization" 헤더를 추가합니다.
            requestTemplate.header("Authorization", token);
        };
    }

}