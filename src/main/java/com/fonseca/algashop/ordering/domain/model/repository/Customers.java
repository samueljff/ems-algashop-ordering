package com.fonseca.algashop.ordering.domain.model.repository;

import com.fonseca.algashop.ordering.domain.model.entity.Customer;
import com.fonseca.algashop.ordering.domain.model.valueObject.Email;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.CustomerId;

import java.util.Optional;

public interface Customers extends Repository<Customer, CustomerId> {
    Optional<Customer> ofEmail(Email email);
}
