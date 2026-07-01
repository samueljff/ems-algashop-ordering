package com.fonseca.algashop.ordering.core.ports.in.customer;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ForManagingCustomers {
    UUID create(CustomerInput input);
    void update(UUID rawCustomerId, CustomerUpdateInput input);
    void archive(UUID rawCustomerId);
    void changeEmail(UUID rawCustomerId, String newEmail);
}
