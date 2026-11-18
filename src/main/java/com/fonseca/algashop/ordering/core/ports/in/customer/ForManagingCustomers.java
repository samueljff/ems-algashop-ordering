package com.fonseca.algashop.ordering.core.ports.in.customer;

import java.util.UUID;

public interface ForManagingCustomers {
    UUID create(UUID customerUserId, CustomerInput input);
    void update(UUID rawCustomerId, CustomerUpdateInput input);
    void archive(UUID rawCustomerId);
    void changeEmail(UUID rawCustomerId, String newEmail);
}
