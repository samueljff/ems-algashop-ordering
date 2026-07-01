package com.fonseca.algashop.ordering.core.application.customer;

import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerFilter;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.fonseca.algashop.ordering.core.ports.in.customer.ForQueryingCustomers;
import com.fonseca.algashop.ordering.core.ports.out.customer.ForObtainingCustomers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerQueryService implements ForQueryingCustomers {

    private final ForObtainingCustomers  forObtainingCustomers;

    @Override
    public CustomerOutput findById(UUID customerId) {
        return forObtainingCustomers.findById(customerId);
    }

    @Override
    public Page filter(CustomerFilter filter) {
        return forObtainingCustomers.filter(filter);
    }
}
