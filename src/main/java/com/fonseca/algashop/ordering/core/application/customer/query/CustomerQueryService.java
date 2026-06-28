package com.fonseca.algashop.ordering.core.application.customer.query;

import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CustomerQueryService {
    CustomerOutput findById(UUID customerId);
    Page filter(CustomerFilter filter);
}
