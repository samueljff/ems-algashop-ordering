package com.fonseca.algashop.ordering.utils;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtSecurityContextFactory.class)
public @interface WithMockJwt {

    String subject() default "6e148bd5-47f6-4022-b9da-07cfaa294f7a";
    String[] scopes() default {
        "orders:read",
        "orders:write",
        "shopping-carts:read",
        "shopping-carts:write",
        "customers:read",
        "customers:write"
    };
    String role() default "CUSTOMER";
    String[] audiences() default {"ecommerce-web-app"};
}
