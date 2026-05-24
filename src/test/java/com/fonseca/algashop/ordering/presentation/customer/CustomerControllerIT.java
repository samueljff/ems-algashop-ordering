package com.fonseca.algashop.ordering.presentation.customer;

import com.fonseca.algashop.ordering.application.customer.management.CustomerInput;
import com.fonseca.algashop.ordering.application.customer.management.CustomerInputTestDataBuilder;
import com.fonseca.algashop.ordering.application.customer.query.CustomerOutput;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.fonseca.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static io.restassured.config.JsonConfig.jsonConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:db/testdata/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:db/clean/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class CustomerControllerIT {

    @LocalServerPort
    private int port;

    private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @BeforeEach
    public void setup() {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        RestAssured.config().jsonConfig(
            jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)
        );

    }

    @Test
    public void shouldCreateCustomer() {

        String json = AlgaShopResourceUtils.readContent("json/create-customer.json");

        UUID customerId = RestAssured
            .given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .when()
            .post("/api/v1/customers")
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

        RestAssured
            .given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .when()
            .post("/api/v1/customers")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldArchiveCustomer() {
        RestAssured
            .given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/api/v1/customers/{customerId}", validCustomerId)
            .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

        Optional<CustomerPersistenceEntity> customer =
            customerRepository.findById(validCustomerId);

        Assertions.assertThat(customer).isPresent();
        Assertions.assertThat(customer.get().getArchived()).isTrue();
    }

    @Test
    public void shouldNotArchiveCustomerWhenCustomerDoesNotExist() {

        UUID customerId = UUID.randomUUID();

        RestAssured
            .given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/api/v1/customers/{customerId}", customerId)
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldCreateCustomer_DTO() {

        CustomerInput input = CustomerInputTestDataBuilder
            .aCustomer()
            .email("johndoe1234@email.com")
            .build();

        CustomerOutput customerOutput =
            RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(input)
                .when()
                .post("/api/v1/customers")
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
}