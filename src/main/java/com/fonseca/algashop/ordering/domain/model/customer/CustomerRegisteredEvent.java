package com.fonseca.algashop.ordering.domain.model.customer;

import java.time.OffsetDateTime;

public record CustomerRegisteredEvent(CustomerId customerId, OffsetDateTime registeredAt) {
}
