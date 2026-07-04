package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.order;

import com.fonseca.algashop.ordering.core.ports.in.checkout.BuyNowInput;
import com.fonseca.algashop.ordering.core.application.checkout.BuyNowInputTestDataBuilder;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.fonseca.algashop.ordering.core.domain.model.order.OrderId;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.order.OrderPersistenceEntityRepository;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartPersistenceEntityRepository;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.AbstractPresentationIT;
import com.fonseca.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

//@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.fonseca.algashop:product-catalog:0.0.1-SNAPSHOT:8781")
public class OrderControllerIT extends AbstractPresentationIT {

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @Autowired
    private ShoppingCartPersistenceEntityRepository shoppingCartRepository;

    private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");
    private static final UUID validProductId = UUID.fromString("fffe6ec2-7103-48b3-8e4f-3b58e43fb75a");
    private static final UUID validShoppingCartId = UUID.fromString("4f31582a-66e6-4601-a9d3-ff608c2d4461");


    @Autowired
    private OrderPersistenceEntityRepository orderRepository;

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

    /**
     * Testes de integração criando pedido usando product
     **/

    @Test
    public void shouldCreateOrderUsingProduct() {
        String json = AlgaShopResourceUtils.readContent("json/create-order-with-product.json");
        String createOrderId = RestAssured
            .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-product.v1+json")
                .body(json)
            .when()
                .post("/api/v1/orders")
            .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value())
                .body(
                "id", Matchers.not(Matchers.emptyString()),
                "customer.id", Matchers.is(validCustomerId.toString()))
            .extract().jsonPath().getString("id");

        boolean orderExists = orderRepository.existsById(new OrderId(createOrderId).value().toLong());

        Assertions.assertThat(orderExists).isTrue();
    }

    @Test
    public void shouldNotCreateOrderUsingProductWhenProductNotExists() {
        String json = AlgaShopResourceUtils.readContent("json/create-order-with-invalid-product.json");

        RestAssured
            .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-product.v1+json")
                .body(json)
            .when()
                .post("/api/v1/orders")
            .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

    }

    @Test
    public void shouldNotCreateOrderUsingProductWhenCustomerWasNotFound() {
        String json = AlgaShopResourceUtils.readContent("json/create-order-with-product-and-invalid-customer.json");
        RestAssured
            .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-product.v1+json")
                .body(json)
            .when()
                .post("/api/v1/orders")
            .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void shouldCreateOrderUsingProduct_DTO() {
        UUID creditCardId = UUID.randomUUID();
        BuyNowInput input = BuyNowInputTestDataBuilder.aBuyNowInput()
            .productId(validProductId)
            .customerId(validCustomerId)
            .creditCardId(creditCardId)
            .build();

        OrderDetailOutput orderDetailOutput = RestAssured
            .given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType("application/vnd.order-with-product.v1+json")
            .body(input)
            .when()
            .post("/api/v1/orders")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.CREATED.value())
            .body("id", Matchers.not(Matchers.emptyString()),
                "customer.id", Matchers.is(validCustomerId.toString()))
            .extract()
            .body().as(OrderDetailOutput.class);

        Assertions.assertThat(orderDetailOutput.getCustomer().getId()).isEqualTo(validCustomerId);
        Assertions.assertThat(orderDetailOutput.getCreditCardId()).isEqualTo(creditCardId);

        boolean orderExists = orderRepository.existsById(new OrderId(orderDetailOutput.getId()).value().toLong());
        Assertions.assertThat(orderExists).isTrue();
    }

    /**
    * Testes de integração criando pedido usando shoppingCart
    * */

    @Test
    public void shouldCreateOrderUsingShoppingCart() {

        String json = AlgaShopResourceUtils.readContent("json/create-order-with-shopping-cart.json");

        OrderDetailOutput orderDetailOutput = RestAssured
            .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-shopping-cart.v1+json")
                .body(json)
            .when()
                .post("/api/v1/orders")
            .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.not(Matchers.emptyString()),
                    "customer.id", Matchers.is(validCustomerId.toString()))
            .extract()
            .body().as(OrderDetailOutput.class);

        Assertions.assertThat(orderDetailOutput).isNotNull();

        Assertions.assertThat(orderDetailOutput.getId()).isNotBlank();

        Assertions.assertThat(orderDetailOutput.getCustomer()).isNotNull();
        Assertions.assertThat(orderDetailOutput.getCustomer().getFirstName()).isEqualTo("John");
        Assertions.assertThat(orderDetailOutput.getCustomer().getLastName()).isEqualTo("Doe");
        Assertions.assertThat(orderDetailOutput.getCustomer().getDocument()).isEqualTo("25508578");
        Assertions.assertThat(orderDetailOutput.getCustomer().getId()).isEqualTo(validCustomerId);

        boolean existOrderId = orderRepository.existsById(new OrderId(orderDetailOutput.getId()).value().toLong());
        Assertions.assertThat(existOrderId).isTrue();
    }

    @Test
    public void shouldNotCreateOrderWhenShoppingCartDoesNotExist() {

        String json = AlgaShopResourceUtils.readContent("json/create-order-with-invalid-shopping-cart.json");

        RestAssured
            .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-shopping-cart.v1+json")
                .body(json)
            .when()
                .post("/api/v1/orders")
            .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}
