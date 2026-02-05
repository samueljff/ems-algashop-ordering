package com.fonseca.algashop.ordering.infrastructure.persistence.customer;

import com.fonseca.algashop.ordering.application.customer.query.CustomerOutput;
import com.fonseca.algashop.ordering.application.customer.query.CustomerQueryService;
import com.fonseca.algashop.ordering.application.utility.Mapper;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final CustomerPersistenceEntityRepository repository;
    private final Mapper mapper;

    @Override
    public CustomerOutput findById(UUID customerId) {
        Objects.requireNonNull(customerId);
        CustomerPersistenceEntity customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());

        return mapper.convert(customer, CustomerOutput.class);
    }
}
