package com.fonseca.algashop.ordering.infrastructure.config.security.check;

import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service("securityChecks")
@Slf4j
public class OAuth2SecurityChecksImpl implements SecurityChecks {

    private static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";

    @Override
    public UUID getAuthenticatedUserId() {
        if (isMachineAuthenticated()) {
            throw new AccessDeniedException("Machine users do not have user ID");
        }

        Jwt jwt = getJwt();
        try {
            return UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException e) {
            log.error("Invalid user ID in JWT subject: {}", jwt.getSubject(), e);
            throw new AccessDeniedException("Invalid user ID in JWT subject");
        }
    }

    @Override
    public boolean isAuthenticated() {
        try {
            return getAuthentication().isAuthenticated();
        } catch (IllegalStateException e) {
            log.debug(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isMachineAuthenticated() {
        Jwt jwt;
        try {
            jwt = getJwt();
        } catch (IllegalStateException e) {
            log.debug(e.getMessage(), e);
            return false;
        }

        return jwt.getAudience().contains(jwt.getSubject());
    }

    @Override
    public boolean isCustomer() {
        return hasAuthority(ROLE_CUSTOMER);
    }

    @Override
    public boolean canOrderFor(UUID customerId) {
        if (customerId == null) {
            return false;
        }
        return isCustomer() && getAuthenticatedUserId().equals(customerId);
    }

    private boolean hasAuthority(String rawAuthority) {
        Authentication authentication;
        try {
            authentication = getAuthentication();
        } catch (IllegalStateException e) {
            log.debug(e.getMessage(), e);
            return false;
        }

        return authentication.getAuthorities()
            .stream().anyMatch(a -> Objects.equals(a.getAuthority(), rawAuthority));
    }

    private Jwt getJwt() {
        Authentication authentication = getAuthentication();
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }

        throw new IllegalStateException("Authentication principal is not a JWT");
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authentication found");
        }

        return authentication;
    }
}