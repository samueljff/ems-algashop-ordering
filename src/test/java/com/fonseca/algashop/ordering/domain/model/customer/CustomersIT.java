package com.fonseca.algashop.ordering.domain.model.customer;

import com.fonseca.algashop.ordering.domain.model.AbstractDomainIT;
import com.fonseca.algashop.ordering.domain.model.commons.Email;
import com.fonseca.algashop.ordering.domain.model.commons.FullName;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import({CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class})
class CustomersIT extends AbstractDomainIT {

    private Customers customers;

    @Autowired
    public CustomersIT(Customers customers) {
        this.customers = customers;
    }

    @Test
    public void shouldPersistCustomerAndRetrieveById() {
        Customer originalCustomer = CustomerTestDataBuilder.brandNewCustomer().build();
        CustomerId customerId = originalCustomer.id();
        customers.add(originalCustomer);

        Optional<Customer> possibleCustomer = customers.ofId(customerId);

        assertThat(possibleCustomer).isPresent();

        Customer savedCustomer = possibleCustomer.get();

        assertThat(savedCustomer).satisfies(
                s -> assertThat(s.id()).isEqualTo(customerId)
        );
    }

    @Test
    public void shouldUpdateCustomer_whenCustomerAlreadyExists() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        customer = customers.ofId(customer.id()).orElseThrow();
        customer.archive();

        customers.add(customer);

        Customer savedCustomer = customers.ofId(customer.id()).orElseThrow();

        assertThat(savedCustomer.archivedAt()).isNotNull();
        assertThat(savedCustomer.isArchived()).isTrue();

    }

    @Test
    public void shouldThrowOptimisticLockingException_whenUpdatingWithStaleVersion() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Customer customerT1 = customers.ofId(customer.id()).orElseThrow();
        Customer customerT2 = customers.ofId(customer.id()).orElseThrow();

        customerT1.archive();
        customers.add(customerT1);

        customerT2.changeName(new FullName("Antônio", "Costa"));

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> customers.add(customerT2));

        Customer savedCustomer = customers.ofId(customer.id()).orElseThrow();

        assertThat(savedCustomer.archivedAt()).isNotNull();
        assertThat(savedCustomer.isArchived()).isTrue();

    }

    @Test
    public void shouldCountCustomers_whenCustomersExist() {
        assertThat(customers.count()).isZero();

        Customer customer1 = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer1);

        Customer customer2 = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer2);

        assertThat(customers.count()).isEqualTo(2L);
    }

    @Test
    public void shouldValidateCustomerExistenceById() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        assertThat(customers.exists(customer.id())).isTrue();
        assertThat(customers.exists(new CustomerId())).isFalse();
    }

    @Test
    public void shouldFindByEmail() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Optional<Customer> customerOptional = customers.ofEmail(customer.email());

        Assertions.assertThat(customerOptional).isPresent();
    }

    @Test
    public void shouldNotFindByEmailIfNoCustomerExistsWithEmail() {
        Optional<Customer> customerOptional = customers.ofEmail(new Email(UUID.randomUUID() + "@email.com"));
        Assertions.assertThat(customerOptional).isNotPresent();
    }

    @Test
    public void shouldReturnIfEmailIsInUse() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Assertions.assertThat(customers.isEmailUnique(customer.email(), customer.id())).isTrue();
        Assertions.assertThat(customers.isEmailUnique(customer.email(), new CustomerId())).isFalse();
        Assertions.assertThat(customers.isEmailUnique(new Email("alex@gmail.com"), new CustomerId())).isTrue();
    }
}