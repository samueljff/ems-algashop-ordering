package com.fonseca.algashop.ordering.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public class MockJwtDecoderConfig {

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        return MockJwtDecoderFactory.createMockJwtDecoder();
    }

}