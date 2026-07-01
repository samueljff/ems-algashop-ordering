package com.fonseca.algashop.ordering.core.ports.in.customer;

import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ForQueryingCustomers {
    CustomerOutput findById(UUID customerId);
    Page filter(CustomerFilter filter);
}
