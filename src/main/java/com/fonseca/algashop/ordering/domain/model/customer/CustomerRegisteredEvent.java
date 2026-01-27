package com.fonseca.algashop.ordering.domain.model.customer;

import com.fonseca.algashop.ordering.domain.model.commons.Email;
import com.fonseca.algashop.ordering.domain.model.commons.FullName;

import java.time.OffsetDateTime;

public record CustomerRegisteredEvent(
        CustomerId customerId,
        OffsetDateTime registeredAt,
        FullName fullName,
        Email email
) {}
