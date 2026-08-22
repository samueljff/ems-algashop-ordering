package com.fonseca.algashop.ordering.contract.base;

import com.fonseca.algashop.ordering.core.application.shoppingcart.ShoppingCartManagementApplicationService;
import com.fonseca.algashop.ordering.core.application.shoppingcart.ShoppingCartOutputTestDataBuilder;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.shoppingcart.ShoppingCartController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = ShoppingCartController.class)
public class ShoppingCartBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ShoppingCartManagementApplicationService managementApplicationService;

    @MockitoBean
    private ForQueryingShoppingCarts queryService;

    public static final UUID validShoppingCartId = UUID.fromString("b9e23c1d-48fa-4d7b-a365-8c1f05e92b47");
    private static final UUID validItemId = UUID.fromString("f5ab7a1e-37da-41e1-892b-a1d38275c2f2");
    public static final UUID notFoundShoppingCartId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final UUID notFoundItemId = UUID.fromString("00000000-0000-0000-0000-000000000000");


    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                        .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockCreateShoppingCart();
        mockFindById();
        mockFindByIdNotFound();
        mockDelete();
        mockDeleteNotFound();
        mockRemoveItem();
        mockRemoveItemNotFound();
        mockEmpty();
        mockEmptyNotFound();
    }

    private void mockEmpty() {
        Mockito.doNothing()
            .when(managementApplicationService)
            .empty(validShoppingCartId);
    }

    private void mockEmptyNotFound() {
        Mockito.doThrow(new ShoppingCartNotFoundException())
            .when(managementApplicationService)
            .empty(notFoundShoppingCartId);
    }

    private void mockRemoveItemNotFound() {
        Mockito.doThrow(new ShoppingCartNotFoundException())
            .when(managementApplicationService)
            .removeItem(validShoppingCartId, notFoundItemId);
    }

    private void mockRemoveItem() {
        Mockito.doNothing()
            .when(managementApplicationService)
            .removeItem(validShoppingCartId, validItemId);
    }

    private void mockDelete() {
        Mockito.doNothing()
            .when(managementApplicationService)
            .delete(validShoppingCartId);
    }

    private void mockDeleteNotFound() {
        Mockito.doThrow(new ShoppingCartNotFoundException())
            .when(managementApplicationService)
            .delete(notFoundShoppingCartId);
    }

    private void mockFindByIdNotFound() {
        Mockito.when(queryService.findById(notFoundShoppingCartId))
                .thenThrow(new ShoppingCartNotFoundException());
    }

    private void mockFindById() {
        Mockito.when(queryService.findById(validShoppingCartId))
                .thenReturn(ShoppingCartOutputTestDataBuilder.aShoppingCart().id(validShoppingCartId).build());
    }

    private void mockCreateShoppingCart() {
        Mockito.when(managementApplicationService.createNew(any(UUID.class)))
            .thenReturn(validShoppingCartId);
    }
}