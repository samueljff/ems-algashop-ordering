package com.fonseca.algashop.ordering.core.application.security;

import java.util.UUID;

public interface SecurityCheckApplicationService {
    UUID getAuthenticatedUserId();
    boolean isAuthenticated();
    boolean isMachineAuthenticated();
}