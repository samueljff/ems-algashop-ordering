package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.customer;

import com.fonseca.algashop.ordering.core.application.customer.CustomerInputTestDataBuilder;
import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerInput;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.fonseca.algashop.ordering.core.ports.in.customer.ForQueryingCustomers;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.AbstractPresentationIT;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import com.fonseca.algashop.ordering.utils.AlgaShopResourceUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

public class CustomerControllerIT extends AbstractPresentationIT {

    private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @Autowired
    private ForQueryingCustomers forQueryingCustomers;

    @MockitoBean
    private SecurityChecks securityChecks;

    @BeforeEach
    public void setup() {
        super.beforeEach();
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(UUID.randomUUID());
    }

    @Test
    public void shouldCreateCustomer() {
        String json = AlgaShopResourceUtils.readContent("json/create-customer.json");

        UUID customerId = givenAuthenticated(securityChecks.getAuthenticatedUserId())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .when()
            .post("/api/v1/customers/me")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.CREATED.value())
            .body(
                "id", Matchers.not(Matchers.emptyString()),
                "firstName", Matchers.is("Marina"),
                "email", Matchers.is("marina.costa@email.com")
            )
            .extract()
            .jsonPath()
            .getUUID("id");

        Optional<CustomerPersistenceEntity> customer =
            customerRepository.findById(UUID.fromString(customerId.toString()));

        Assertions.assertThat(customer).isPresent();
        Assertions.assertThat(customer.get().getEmail())
            .isEqualTo("marina.costa@email.com");
    }

    @Test
    public void shouldNotCreateCustomerWithInvalidData() {
        String json = AlgaShopResourceUtils.readContent("json/create-customer-invalid-data.json");

        givenAuthenticated(securityChecks.getAuthenticatedUserId())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .when()
            .post("/api/v1/customers/me")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

//    @Test
//    public void shouldArchiveCustomer() {
//        givenAuthenticated()
//            .accept(MediaType.APPLICATION_JSON_VALUE)
//            .when()
//            .delete("/api/v1/customers/{customerId}", validCustomerId)
//            .then()
//            .assertThat()
//            .statusCode(HttpStatus.NO_CONTENT.value());
//
//        Optional<CustomerPersistenceEntity> customer =
//            customerRepository.findById(validCustomerId);
//
//        Assertions.assertThat(customer).isPresent();
//        Assertions.assertThat(customer.get().getArchived()).isTrue();
//    }

//    @Test
//    public void shouldNotArchiveCustomerWhenCustomerDoesNotExist() {
//
//        UUID customerId = UUID.randomUUID();
//
//        givenAuthenticated()
//            .accept(MediaType.APPLICATION_JSON_VALUE)
//            .when()
//            .delete("/api/v1/customers/{customerId}", customerId)
//            .then()
//            .assertThat()
//            .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
//            .statusCode(HttpStatus.NOT_FOUND.value());
//    }

    @Test
    public void shouldCreateCustomer_DTO() {
        CustomerInput input = CustomerInputTestDataBuilder
            .aCustomer()
            .email("johndoe1234@email.com")
            .build();

        CustomerOutput customerOutput =
            givenAuthenticated(securityChecks.getAuthenticatedUserId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(input)
                .when()
                .post("/api/v1/customers/me")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value())
                .body(
                    "id", Matchers.not(Matchers.emptyString()),
                    "email", Matchers.is(input.getEmail())
                )
                .extract()
                .body()
                .as(CustomerOutput.class);

        Assertions.assertThat(customerOutput.getId()).isNotNull();

        Assertions.assertThat(customerOutput.getEmail()).isEqualTo(input.getEmail());

        boolean customerExists = customerRepository.existsById(customerOutput.getId());

        Assertions.assertThat(customerExists).isTrue();
    }

    @Test
    public void shouldReturnForbiddenWhenCreatingCustomerWithoutWriteScope() {
        String json = AlgaShopResourceUtils.readContent("json/create-customer.json");

        givenAuthenticatedWithNoScopeToken(securityChecks.getAuthenticatedUserId())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .when()
            .post("/api/v1/customers/me")
            .then()
            .assertThat()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void shouldReturnUnauthorizedWhenExpiredTokenIsGiven() {
        String json = AlgaShopResourceUtils.readContent("json/create-customer.json");

        givenWithExpiredToken()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .when()
            .post("/api/v1/customers/me")
            .then()
            .assertThat()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }


    @Test
    public void shouldFindAuthenticatedCustomer() {
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(validCustomerId);
        UUID authenticatedId = securityChecks.getAuthenticatedUserId();
        CustomerOutput expected = forQueryingCustomers.findById(authenticatedId);

        CustomerOutput actual = givenAuthenticated(authenticatedId)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/v1/customers/me")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(CustomerOutput.class);

        Assertions.assertThat(actual.getId()).isEqualTo(authenticatedId);
        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        Assertions.assertThat(actual.getDocument()).isEqualTo(expected.getDocument());
        Assertions.assertThat(actual.getBirthDate()).isEqualTo(expected.getBirthDate());
        Assertions.assertThat(actual.getArchived()).isFalse();
    }

    @Test
    public void shouldUpdateAuthenticatedCustomer() {
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(validCustomerId);
        String json = """
            {
              "firstName": "Marina Atualizada",
              "lastName": "Costa",
              "email": "marina.costa@email.com",
              "phone": "1191234564",
              "promotionNotificationsAllowed": true,
              "address": {
                "street": "Bourbon Street",
                "number": "2000",
                "complement": "apt 122",
                "neighborhood": "North Ville",
                "city": "Yostfort",
                "state": "South Carolina",
                "zipCode": "12321"
              }
            }
            """;

        CustomerOutput output = givenAuthenticated(validCustomerId)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .when()
            .put("/api/v1/customers/me")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.OK.value())
            .body(
                "id", Matchers.equalTo(validCustomerId.toString()),
                "firstName", Matchers.equalTo("Marina Atualizada"),
                "promotionNotificationsAllowed", Matchers.is(true)
            )
            .extract()
            .as(CustomerOutput.class);

        Assertions.assertThat(output.getId()).isEqualTo(validCustomerId);

        CustomerPersistenceEntity customer = customerRepository
            .findById(validCustomerId)
            .orElseThrow();

        Assertions.assertThat(customer.getFirstName())
            .isEqualTo("Marina Atualizada");
    }
}