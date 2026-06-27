package com.fonseca.algashop.ordering.presentation.shoppingcart;

import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.fonseca.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntityRepository;
import com.fonseca.algashop.ordering.presentation.AbstractPresentationIT;
import com.fonseca.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Optional;
import java.util.UUID;

public class ShoppingCartControllerIT extends AbstractPresentationIT {

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @Autowired
    private ShoppingCartPersistenceEntityRepository shoppingCartRepository;

    private static final UUID validCustomerId = UUID.fromString("3a4b5c6d-7e8f-9a0b-1c2d-3e4f5a6b7c8d");
    private static final UUID validShoppingCartId = UUID.fromString("4f31582a-66e6-4601-a9d3-ff608c2d4461");

    @BeforeEach
    public void setup() {
        super.beforeEach();
    }

    @BeforeAll
    public static void setupBeforeAll() {
        initWireMock();
    }

    @AfterAll
    public static void afterAll() {
        stopMock();
    }

    @Test
    public void shouldCreateShoppingCart() {

        String json = AlgaShopResourceUtils.readContent("json/create-shopping-cart.json");

        UUID shoppingCartId = RestAssured
            .given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .when()
            .post("/api/v1/shopping-carts")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.CREATED.value())
            .body(
                "id", Matchers.not(Matchers.emptyString()),
                "customerId", Matchers.is(validCustomerId.toString())
            )
            .extract().jsonPath().getUUID("id");

        Optional<ShoppingCartPersistenceEntity> shoppingCart = shoppingCartRepository.findById(shoppingCartId);

        Assertions.assertThat(shoppingCart).isPresent();
    }

    @Test
    public void shouldNotCreateShoppingCartWithInvalidData() {

        String json = UUID.randomUUID().toString();

        RestAssured
            .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
            .when()
                .post("/api/v1/shopping-carts")
            .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldAddItemToExistingShoppingCart() {
        String json = AlgaShopResourceUtils.readContent("json/add-item-to-shopping-cart.json");
        RestAssured
            .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
            .when()
                .post("/api/v1/shopping-carts/{shoppingCartId}/items", validShoppingCartId)
            .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var updatedShoppingCart = shoppingCartRepository.findById(validShoppingCartId).orElseThrow();

        Assertions.assertThat(updatedShoppingCart.getItems()).hasSize(1);

        Assertions.assertThat(updatedShoppingCart.getTotalItems()).isEqualTo(5);
    }

    @Test
    public void shouldNotAddItemWhenShoppingCartDoesNotExist() {

        String json = AlgaShopResourceUtils.readContent("json/add-item-to-shopping-cart.json");
        RestAssured
            .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
            .when()
                .post("/api/v1/shopping-carts/{shoppingCartId}/items", UUID.randomUUID().toString())
            .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}