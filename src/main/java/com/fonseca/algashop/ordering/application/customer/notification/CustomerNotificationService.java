package com.fonseca.algashop.ordering.application.customer.notification;

import java.util.UUID;

public interface CustomerNotificationService {
    void notifyNewRegistration(UUID customerId);
}
