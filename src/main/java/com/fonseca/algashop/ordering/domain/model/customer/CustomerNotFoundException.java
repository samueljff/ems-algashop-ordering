package com.fonseca.algashop.ordering.domain.model.customer;

import com.fonseca.algashop.ordering.domain.model.DomainEntityNotFoundException;

import static com.fonseca.algashop.ordering.domain.model.ErrorMessages.ERROR_CUSTOMER_NOT_FOUND;

public class CustomerNotFoundException extends DomainEntityNotFoundException {
    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(CustomerId customerId) {
        super(String.format(ERROR_CUSTOMER_NOT_FOUND, customerId));
    }
}
