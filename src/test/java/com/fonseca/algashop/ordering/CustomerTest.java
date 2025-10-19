package com.fonseca.algashop.ordering;


import com.fonseca.algashop.ordering.domain.entity.Customer;
import com.fonseca.algashop.ordering.domain.utility.IdGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTest {

    @Test
    public void testingCustomer() {
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "Jhon Doe",
                LocalDate.of(1991, 7, 5),
                "jhon.doe@email.com",
                "478-256-2504",
                "255-08-0578",
                true,
                OffsetDateTime.now()
        );
        System.out.println(customer.id());
        System.out.println(IdGenerator.generateTimeBasedUUID());
        customer.addLoayltyPoints(10);
    }
}
