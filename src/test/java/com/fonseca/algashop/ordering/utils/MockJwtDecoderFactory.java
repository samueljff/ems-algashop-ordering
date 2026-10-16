package com.fonseca.algashop.ordering.utils;

import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class MockJwtDecoderFactory {

    public static final String DEFAULT_ISSUER_URI = "http://algashop-authorization-server:8081";

    public static final String[] DEFAULT_SCOPES = new String[]{
        "orders:read",
        "orders:write,",
        "customers:read",
        "customers:write",
        "shopping-carts:read",
        "shopping-carts:write",
        "shipping-costs:preview"
    };

    public static final String DEFAULT_SUBJECT = "test-user";
    public static final String DEFAULT_TOKEN_VALUE = "fake.jwt.token";

    public static JwtDecoder createMockJwtDecoder() {
        return createMockJwtDecoder(DEFAULT_SUBJECT, DEFAULT_ISSUER_URI, DEFAULT_SCOPES);
    }

    public static JwtDecoder createMockJwtDecoder(String subject, String issuer, String[] scopes) {
        JwtDecoder jwtDecoder = Mockito.mock(JwtDecoder.class);
        Mockito.when(jwtDecoder.decode(Mockito.anyString()))
            .thenReturn(buildJwt(subject, issuer, scopes));
        return jwtDecoder;
    }

    public static Jwt buildJwt(String subject, String issuer, String[] scopes) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(600);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        claims.put("iss", issuer);

        if (scopes != null && scopes.length > 0) {
            claims.put("scope", String.join(" ", scopes));
        }

        return Jwt.withTokenValue(DEFAULT_TOKEN_VALUE)
            .issuedAt(now)
            .expiresAt(expiresAt)
            .issuer(issuer)
            .subject(subject)
            .claims(c -> c.putAll(claims))
            .headers(h -> h.put("alg", "none"))
            .build();
    }
}
