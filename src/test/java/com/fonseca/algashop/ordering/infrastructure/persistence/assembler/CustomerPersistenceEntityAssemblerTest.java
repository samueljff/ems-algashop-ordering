package com.fonseca.algashop.ordering.infrastructure.persistence.assembler;

import com.fonseca.algashop.ordering.domain.model.entity.Customer;
import com.fonseca.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerPersistenceEntityAssemblerTest {

    private final CustomerPersistenceEntityAssembler assembler = new CustomerPersistenceEntityAssembler();

    @Test
    void shouldConvertFromPersistence() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        CustomerPersistenceEntity customerPersistenceEntity = assembler.fromDomain(customer);
        assertThat(customerPersistenceEntity).satisfies(
                c -> assertThat(c.getId()).isEqualTo(customer.id().value()),
                c -> assertThat(c.getFirstName()).isEqualTo(customer.fullName().firstName()),
                c -> assertThat(c.getLastName()).isEqualTo(customer.fullName().lastName()),
                c -> assertThat(c.getEmail()).isEqualTo(customer.email().value()),
                c -> assertThat(c.getRegisteredAt()).isEqualTo(customer.registeredAt()),
                c -> assertThat(c.getVersion()).isEqualTo(customer.version())
        );
    }

}
