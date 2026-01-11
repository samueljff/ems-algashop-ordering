package com.fonseca.algashop.ordering.domain.model.customer;

import com.fonseca.algashop.ordering.domain.model.Repository;
import com.fonseca.algashop.ordering.domain.model.commons.Email;

import java.util.Optional;

public interface Customers extends Repository<Customer, CustomerId> {
    Optional<Customer> ofEmail(Email email);
    boolean isEmailUnique(Email email, CustomerId exceptCustomerId);
}
