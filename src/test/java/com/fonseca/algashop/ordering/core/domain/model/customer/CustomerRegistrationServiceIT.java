package com.fonseca.algashop.ordering.core.domain.model.customer;

import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.core.domain.model.AbstractDomainIT;
import com.fonseca.algashop.ordering.core.domain.model.commons.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.UUID;

class CustomerRegistrationServiceIT extends AbstractDomainIT {

    @Autowired
    private CustomerRegistrationService customerRegistrationService;

    @MockitoBean
    private SecurityChecks securityChecks;

    @BeforeEach
    void setup(){
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(UUID.randomUUID());
    }

    @Test
    public void shouldRegister() {
        Customer customer = customerRegistrationService.register(
                new CustomerId(securityChecks.getAuthenticatedUserId()),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1991, 7, 5)),
                new Email("johndoe@email.com"),
                new Phone("478-256-2604"),
                new Document("255-08-0578"),
                true,
                Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("Yostfort")
                        .state("South Carolina")
                        .zipCode(new ZipCode("70283"))
                        .complement("Apt. 901")
                        .build()
        );

        Assertions.assertThat(customer.fullName()).isEqualTo(new FullName("John", "Doe"));
        Assertions.assertThat(customer.email()).isEqualTo(new Email("johndoe@email.com"));
    }
}