package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.shoppingcart;

import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.AbstractPresentationIT;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartPersistenceEntityRepository;
import com.fonseca.algashop.ordering.utils.AlgaShopResourceUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

public class ShoppingCartControllerIT extends AbstractPresentationIT {

    @Autowired
    private ShoppingCartPersistenceEntityRepository shoppingCartRepository;

    @MockitoBean
    private SecurityChecks  securityChecks;

    private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");
    private static final UUID customerIdWithoutShoppingCart = UUID.fromString("3a4b5c6d-7e8f-9a0b-1c2d-3e4f5a6b7c8d");
    private static final UUID invalidCustomerId = UUID.fromString("00000000-0000-9a0b-1c2d-3e4f5a6b7c8d");

    @BeforeEach
    public void setup() {
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(validCustomerId);
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
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(customerIdWithoutShoppingCart);
        String json = customerIdWithoutShoppingCart.toString();

        UUID shoppingCartId = givenAuthenticated(customerIdWithoutShoppingCart)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .when()
            .post("/api/v1/customers/me/shopping-cart")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.CREATED.value())
            .body(
                "id", Matchers.not(Matchers.emptyString()),
                "customerId", Matchers.is(customerIdWithoutShoppingCart.toString())
            )
            .extract().jsonPath().getUUID("id");

        Optional<ShoppingCartPersistenceEntity> shoppingCart = shoppingCartRepository.findById(shoppingCartId);

        Assertions.assertThat(shoppingCart).isPresent();
    }

    @Test
    public void shouldNotCreateShoppingCartWithInvalidData() {
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(invalidCustomerId);
        String json = invalidCustomerId.toString();

        givenAuthenticated(invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
            .when()
                .post("/api/v1/customers/me/shopping-cart")
            .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldAddItemToExistingShoppingCart() {
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(validCustomerId);
        String json = AlgaShopResourceUtils.readContent("json/add-item-to-shopping-cart.json");
        givenAuthenticated(validCustomerId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
            .when()
                .post("/api/v1/customers/me/shopping-cart/items")
            .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var updatedShoppingCart = shoppingCartRepository.findByCustomer_Id(validCustomerId).orElseThrow();

        Assertions.assertThat(updatedShoppingCart.getItems()).hasSize(1);

        Assertions.assertThat(updatedShoppingCart.getTotalItems()).isEqualTo(5);
    }

    @Test
    public void shouldNotAddItemWhenShoppingCartDoesNotExist() {
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(invalidCustomerId);
        String json = AlgaShopResourceUtils.readContent("json/add-item-to-shopping-cart.json");
        givenAuthenticated(invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
            .when()
                .post("/api/v1/customers/me/shopping-cart/items")
            .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }


    @Test
    public void shouldReturnForbiddenWhenCustomerTriesToReadShoppingCartOfAnotherCustomer() {
        givenAuthenticated(validCustomerId)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/v1/customers/{customerId}/shopping-cart", UUID.randomUUID())
            .then()
            .assertThat()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void shouldReadMyShoppingCart() {
        givenAuthenticated(validCustomerId)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/v1/customers/me/shopping-cart")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
    }
}