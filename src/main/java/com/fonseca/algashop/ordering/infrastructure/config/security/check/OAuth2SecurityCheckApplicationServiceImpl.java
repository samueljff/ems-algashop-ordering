package com.fonseca.algashop.ordering.infrastructure.config.security.check;

import com.fonseca.algashop.ordering.core.application.security.SecurityCheckApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityCheck")
@Slf4j
public class OAuth2SecurityCheckApplicationServiceImpl implements SecurityCheckApplicationService {

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