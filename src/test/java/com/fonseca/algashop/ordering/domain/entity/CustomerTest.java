package com.fonseca.algashop.ordering.domain.entity;

import com.fonseca.algashop.ordering.domain.utility.IdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Customer(
                            IdGenerator.generateTimeBasedUUID(),
                            "John Doe",
                            LocalDate.of(1991, 7, 5),
                            "invalid",
                            "478-256-2504",
                            "255-08-0578",
                            false,
                            OffsetDateTime.now()
                    );
                });
    }

    @Test
    void given_invalidEmail_whenTryUpdatedCustomerEmail_shouldGenerateException() {
        Customer customer = new Customer(
            IdGenerator.generateTimeBasedUUID(),
            "John Doe",
            LocalDate.of(1991, 7, 5),
            "samuel@gmail.com",
            "478-256-2504",
            "255-08-0578",
            false,
            OffsetDateTime.now()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> {
                customer.changeEmail("invalid");
            });
    }
}