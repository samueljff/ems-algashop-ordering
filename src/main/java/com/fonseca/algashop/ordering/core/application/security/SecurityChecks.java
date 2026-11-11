package com.fonseca.algashop.ordering.core.application.security;

import java.util.UUID;

public interface SecurityChecks {
    UUID getAuthenticatedUserId();
    boolean isAuthenticated();
    boolean isMachineAuthenticated();
    boolean isCustomer();
    boolean canOrderFor(UUID customerId);
}