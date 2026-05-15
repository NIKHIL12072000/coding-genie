package com.nod.backend.distributed_coding_genie.common_lib.security;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.context.annotation.ComponentScan;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@AutoConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackageClasses = { SharedSecurityAutoConfiguration.class, AuthUtil.class })
public class SharedSecurityAutoConfiguration {

    @Bean
    public AuthUtil authUtil() {
        return new AuthUtil();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(AuthUtil authUtil,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        return new JwtAuthFilter(authUtil, handlerExceptionResolver);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authorizationHeader = request.getHeader("Authorization");
                if (authorizationHeader != null) {
                    requestTemplate.header("Authorization", authorizationHeader);
                }
            }
        };
    }
}
