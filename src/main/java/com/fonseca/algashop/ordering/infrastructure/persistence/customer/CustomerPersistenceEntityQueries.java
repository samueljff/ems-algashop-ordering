package com.fonseca.algashop.ordering.infrastructure.persistence.customer;

import com.fonseca.algashop.ordering.application.customer.query.CustomerOutput;

import java.util.Optional;
import java.util.UUID;

public interface CustomerPersistenceEntityQueries {
    Optional<CustomerOutput> findByIdAsOutput(UUID customerId);
}
