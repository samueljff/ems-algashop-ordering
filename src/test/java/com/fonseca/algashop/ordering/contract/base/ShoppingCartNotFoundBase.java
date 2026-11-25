package com.fonseca.algashop.ordering.contract.base;

import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.core.application.shoppingcart.ShoppingCartManagementApplicationService;
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
public class ShoppingCartNotFoundBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ShoppingCartManagementApplicationService managementApplicationService;

    @MockitoBean
    private ForQueryingShoppingCarts queryService;

    @MockitoBean
    private SecurityChecks securityChecks;

    public static final UUID notFoundCustomerId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @BeforeEach
    void setUp() {
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(notFoundCustomerId);

        RestAssuredMockMvc.mockMvc(
            MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockCreateShoppingCartNotFound();
        mockFindByIdNotFound();
        mockDeleteNotFound();
        mockEmptyNotFound();
    }

    private void mockCreateShoppingCartNotFound() {
        Mockito.when(managementApplicationService.createNew(any(UUID.class)))
            .thenReturn(notFoundCustomerId);
    }

    private void mockEmptyNotFound() {
        Mockito.doThrow(new ShoppingCartNotFoundException())
            .when(managementApplicationService)
            .empty(notFoundCustomerId);
    }

    private void mockDeleteNotFound() {
        Mockito.doThrow(new ShoppingCartNotFoundException())
            .when(managementApplicationService)
            .delete(notFoundCustomerId);
    }

    private void mockFindByIdNotFound() {
        Mockito.when(queryService.findByCustomerId(notFoundCustomerId))
            .thenThrow(new ShoppingCartNotFoundException());
    }
}
