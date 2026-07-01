package com.fonseca.algashop.ordering.core.ports.out.customer;

import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerFilter;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerOutput;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ForObtainingCustomers {
    CustomerOutput findById(UUID customerId);
    Page filter(CustomerFilter filter);
}
