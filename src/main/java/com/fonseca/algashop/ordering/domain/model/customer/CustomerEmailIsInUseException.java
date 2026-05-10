package com.fonseca.algashop.ordering.domain.model.customer;

import com.fonseca.algashop.ordering.domain.model.DomainException;
import com.fonseca.algashop.ordering.domain.model.ErrorMessages;

public class CustomerEmailIsInUseException extends DomainException {

    public CustomerEmailIsInUseException(CustomerId customerId) {
        super(String.format(ErrorMessages.ERROR_CUSTOMER_EMAIL_IS_IN_USE, customerId));
    }
}
