package com.fonseca.algashop.ordering.application.customer.query;

import com.fonseca.algashop.ordering.domain.model.commons.Email;
import com.fonseca.algashop.ordering.domain.model.commons.FullName;
import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.customer.Customers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CustomerQueryServiceIT {

    @Autowired
    private CustomerQueryService queryService;

    @Autowired
    private Customers customers;

    @Test
    void shouldFilterCustomersByFirstNameIgnoringCase() {
        Customer maria1 = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Silva"))
                .build();

        Customer joao = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("João", "Paulo"))
                .build();

        Customer maria2 = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Sousa"))
                .build();

        Customer carlos = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Carlos", "Pereira"))
                .build();

        customers.add(maria1);
        customers.add(joao);
        customers.add(maria2);
        customers.add(carlos);

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("maria");

        Page<CustomerSummaryOutput> result = queryService.filter(filter);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .hasSize(2)
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsOnly("Maria");

        assertThat(result.getContent())
                .extracting(CustomerSummaryOutput::getLastName)
                .containsExactlyInAnyOrder("Silva", "Sousa");
    }

    @Test
    public void shouldFindById() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        CustomerOutput output = queryService.findById(customer.id().value());

        assertThat(output)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getEmail
                ).containsExactly(
                        customer.id().value(),
                        customer.fullName().firstName(),
                        customer.email().value()
                );
    }

    @Test
    public void shouldFilterByPage() {
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Ana", "Silva")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Bruno", "Costa")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Carla", "Souza")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Daniel", "Pereira")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Eduarda", "Santos")).build());

        CustomerFilter filter = new CustomerFilter(2, 0);
        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    public void shouldFilterByEmail() {
        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .email(new Email("alice@company.com"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .email(new Email("company.contact@example.com"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .email(new Email("john@mycompany.org"))
                .build());

        CustomerFilter filter = new CustomerFilter();
        filter.setEmail("company");

        // When
        Page<CustomerSummaryOutput> result = queryService.filter(filter);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent())
                .hasSize(3)
                .extracting(CustomerSummaryOutput::getEmail)
                .containsExactlyInAnyOrder(
                        "alice@company.com",
                        "company.contact@example.com",
                        "john@mycompany.org"
                );
    }

    @Test
    public void shouldApplyMultipleFilterParams() {
        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Carlos", "Silva"))
                .email(new Email("carlos.silva@company.com"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Silva"))
                .email(new Email("maria.silva@company.com"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Carlos", "Santos"))
                .email(new Email("carlos.santos@example.com"))
                .build());

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("carlos");
        filter.setEmail("company");

        // When
        Page<CustomerSummaryOutput> result = queryService.filter(filter);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent())
                .hasSize(1)
                .first()
                .satisfies(customer -> {
                    assertThat(customer.getFirstName()).isEqualTo("Carlos");
                    assertThat(customer.getEmail()).isEqualTo("carlos.silva@company.com");
                });
    }

    @Test
    void shouldReturnCorrectNumberOfElements() {
        for (int i = 1; i <= 25; i++) {
            customers.add(CustomerTestDataBuilder.existingCustomer()
                    .id(new CustomerId())
                    .fullName(new FullName("Customer" + i, "Silva"))
                    .email(new Email("customer" + i + "@example.com"))
                    .build());
        }

        // primeira página (10 elementos)
        CustomerFilter filterPage0 = new CustomerFilter(10, 0);
        Page<CustomerSummaryOutput> page0 = queryService.filter(filterPage0);

        // segunda página (10 elementos)
        CustomerFilter filterPage1 = new CustomerFilter(10, 1);
        Page<CustomerSummaryOutput> page1 = queryService.filter(filterPage1);

        // terceira página (5 elementos restantes)
        CustomerFilter filterPage2 = new CustomerFilter(10, 2);
        Page<CustomerSummaryOutput> page2 = queryService.filter(filterPage2);

        // primeira página
        assertThat(page0.getTotalElements()).isEqualTo(25);
        assertThat(page0.getTotalPages()).isEqualTo(3);
        assertThat(page0.getContent()).hasSize(10);
        assertThat(page0.isFirst()).isTrue();
        assertThat(page0.isLast()).isFalse();

        //segunda página
        assertThat(page1.getTotalElements()).isEqualTo(25);
        assertThat(page1.getContent()).hasSize(10);
        assertThat(page1.isFirst()).isFalse();
        assertThat(page1.isLast()).isFalse();

        //terceira página (última)
        assertThat(page2.getTotalElements()).isEqualTo(25);
        assertThat(page2.getContent()).hasSize(5);
        assertThat(page2.isFirst()).isFalse();
        assertThat(page2.isLast()).isTrue();
    }

    @Test
    void shouldOrderByFirstNameAsc() {
        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Roberto", "Santos"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Beatriz", "Oliveira"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Ana", "Costa"))
                .build());

        CustomerFilter filter = new CustomerFilter();
        filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
        filter.setSortDirection(Sort.Direction.ASC);

        Page<CustomerSummaryOutput> result = queryService.filter(filter);

        assertThat(result.getContent())
                .hasSize(3)
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("Ana", "Beatriz", "Roberto");
    }

    @Test
    public void shouldOrderByFirstNameDesc() {
        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Roberto", "Santos"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Beatriz", "Oliveira"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Ana", "Costa"))
                .build());

        CustomerFilter filter = new CustomerFilter();
        filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
        filter.setSortDirection(Sort.Direction.DESC);

        Page<CustomerSummaryOutput> result = queryService.filter(filter);

        assertThat(result.getContent())
                .hasSize(3)
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("Roberto", "Beatriz", "Ana");
    }

    @Test
    void shouldOrderByRegisteredAtAsc() {
        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Roberto", "Santos"))
                .registeredAt(OffsetDateTime.parse("2024-01-10T10:00:00Z"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Beatriz", "Oliveira"))
                .registeredAt(OffsetDateTime.parse("2024-01-05T10:00:00Z"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Ana", "Costa"))
                .registeredAt(OffsetDateTime.parse("2024-01-01T10:00:00Z"))
                .build());

        CustomerFilter filter = new CustomerFilter();
        filter.setSortByProperty(CustomerFilter.SortType.REGISTERED_AT);
        filter.setSortDirection(Sort.Direction.ASC);

        Page<CustomerSummaryOutput> result = queryService.filter(filter);

        assertThat(result.getContent())
                .hasSize(3)
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("Ana", "Beatriz", "Roberto");
    }

    @Test
    public void shouldOrderByRegisteredAtDesc() {
        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Roberto", "Santos"))
                .registeredAt(OffsetDateTime.parse("2024-01-10T10:00:00Z"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Beatriz", "Oliveira"))
                .registeredAt(OffsetDateTime.parse("2024-01-05T10:00:00Z"))
                .build());

        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Ana", "Costa"))
                .registeredAt(OffsetDateTime.parse("2024-01-01T10:00:00Z"))
                .build());

        CustomerFilter filter = new CustomerFilter();
        filter.setSortByProperty(CustomerFilter.SortType.REGISTERED_AT);
        filter.setSortDirection(Sort.Direction.DESC);

        Page<CustomerSummaryOutput> result = queryService.filter(filter);

        assertThat(result.getContent())
                .hasSize(3)
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("Roberto", "Beatriz", "Ana");
    }

    @Test
    public void shouldReturnEmptyPageWhenNoMatchFound() {
        customers.add(CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Jennifer", "Wilson"))
                .build());

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("Invalid");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.isEmpty()).isTrue();
        assertThat(page.getTotalElements()).isEqualTo(0);
    }

}