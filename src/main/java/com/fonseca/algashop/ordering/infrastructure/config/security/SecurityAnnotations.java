package com.fonseca.algashop.ordering.infrastructure.config.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class SecurityAnnotations {

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_orders:read') and not hasRole('CUSTOMER')")
    public @interface CanReadOrders {}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_customers:read') and not hasRole('CUSTOMER')")
    public @interface CanReadCustomers {}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_shopping-carts:read')")
    public @interface CanReadShoppingCarts {}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_shopping-carts:write')")
    public @interface CanWriteShoppingCarts {}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_shipping-costs:preview')")
    public @interface CanPreviewShippingCost {}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_customers:write') and hasRole('CUSTOMER')")
    public @interface CanWriteMyCustomerProfile {}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_customers:read') and hasRole('CUSTOMER')")
    public @interface CanReadMyCustomerProfile {
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_orders:write') and hasRole('CUSTOMER')")
    public @interface CanWriteMyOrders {}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_orders:read') and hasRole('CUSTOMER')")
    public @interface CanReadMyOrders {}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_shopping-carts:write') and hasRole('CUSTOMER')")
    public @interface CanWriteMyShoppingCart {}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('SCOPE_shopping-carts:read') and hasRole('CUSTOMER')")
    public @interface CanReadMyShoppingCart {}
}