package com.fonseca.algashop.ordering.contract.base;

import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.core.application.shoppingcart.ShoppingCartManagementApplicationService;
import com.fonseca.algashop.ordering.core.application.shoppingcart.ShoppingCartOutputTestDataBuilder;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.shoppingcart.MyShoppingCartController;
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

@WebMvcTest(controllers = {MyShoppingCartController.class})
public class ShoppingCartBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ShoppingCartManagementApplicationService managementApplicationService;

    @MockitoBean
    private ForQueryingShoppingCarts queryService;

    @MockitoBean
    private SecurityChecks securityChecks;

    public static final UUID validCustomerId = UUID.fromString("41cdc65c-6158-48b0-a8e6-34c0ff8fd74e");
    private static final UUID validItemId = UUID.fromString("f5ab7a1e-37da-41e1-892b-a1d38275c2f2");
    public static final UUID notFoundCustomerId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final UUID notFoundItemId = UUID.fromString("00000000-0000-0000-0000-000000000000");


    @BeforeEach
    void setUp() {

        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(validCustomerId);

        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                        .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockCreateShoppingCart();
        mockFindById();
        mockDelete();
        mockRemoveItem();
        mockRemoveItemNotFound();
        mockEmpty();
    }

    private void mockEmpty() {
        Mockito.doNothing()
            .when(managementApplicationService)
            .empty(validCustomerId);
    }

    private void mockRemoveItemNotFound() {
        Mockito.doThrow(new ShoppingCartNotFoundException())
            .when(managementApplicationService)
            .removeItem(validCustomerId, notFoundItemId);
    }

    private void mockRemoveItem() {
        Mockito.doNothing()
            .when(managementApplicationService)
            .removeItem(validCustomerId, validItemId);
    }

    private void mockDelete() {
        Mockito.doNothing()
            .when(managementApplicationService)
            .delete(validCustomerId);
    }

    private void mockFindById() {
        Mockito.when(queryService.findByCustomerId(validCustomerId))
                .thenReturn(ShoppingCartOutputTestDataBuilder.aShoppingCart().id(validCustomerId).build());
    }

    private void mockCreateShoppingCart() {
        Mockito.when(managementApplicationService.createNew(any(UUID.class)))
            .thenReturn(validCustomerId);
    }
}