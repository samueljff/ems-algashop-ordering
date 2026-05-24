package com.fonseca.algashop.ordering.presentation.shoppingcart;

import com.fonseca.algashop.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import com.fonseca.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import com.fonseca.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntityRepository;
import com.fonseca.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntityTestDataBuilder;
import com.fonseca.algashop.ordering.utils.AlgaShopResourceUtils;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.config.JsonConfig.jsonConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:db/clean/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ShoppingCartControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @Autowired
    private ShoppingCartPersistenceEntityRepository shoppingCartRepository;

    private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");

    private static final UUID validProductId = UUID.fromString("fffe6ec2-7103-48b3-8e4f-3b58e43fb75a");

    private WireMockServer wireMockProductCatalog;

    @BeforeEach
    public void setup() {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        RestAssured.config().jsonConfig(
            jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)
        );

        initDatabase();

        wireMockProductCatalog = new WireMockServer(options()
            .port(8781)
            .usingFilesUnderDirectory("src/test/resources/wiremock/product-catalog")
            .extensions(new ResponseTemplateTransformer(true))
        );

        wireMockProductCatalog.start();
    }

    @AfterEach
    public void after() {
        wireMockProductCatalog.stop();
    }

    private void initDatabase() {

        customerRepository.saveAndFlush(
            CustomerPersistenceEntityTestDataBuilder
                .aCustomer()
                .id(validCustomerId)
                .build()
        );
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

        var shoppingCart = ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart()
            .items(new HashSet<>())
            .customer(customerRepository.getReferenceById(validCustomerId))
            .build();

        shoppingCartRepository.saveAndFlush(shoppingCart);

        UUID shoppingCartId = shoppingCart.getId();

        String json = AlgaShopResourceUtils.readContent("json/add-item-to-shopping-cart.json");
        RestAssured
            .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
            .when()
                .post("/api/v1/shopping-carts/{shoppingCartId}/items", shoppingCartId)
            .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var updatedShoppingCart = shoppingCartRepository.findById(shoppingCartId).orElseThrow();

        Assertions.assertThat(updatedShoppingCart.getItems()).hasSize(1);

        Assertions.assertThat(updatedShoppingCart.getTotalItems()).isEqualTo(3);
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