package com.fonseca.algashop.ordering.contract.base;

import com.fonseca.algashop.ordering.core.application.checkout.BuyNowApplicationService;
import com.fonseca.algashop.ordering.core.application.checkout.CheckoutApplicationService;
import com.fonseca.algashop.ordering.core.application.order.OrderDetailOutputTestDataBuilder;
import com.fonseca.algashop.ordering.core.application.order.OrderSummaryOutputTestDataBuilder;
import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.core.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.fonseca.algashop.ordering.core.ports.in.checkout.BuyNowInput;
import com.fonseca.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import com.fonseca.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.fonseca.algashop.ordering.core.ports.in.order.OrderFilter;
import com.fonseca.algashop.ordering.core.ports.out.order.CustomerMinimalOutput;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.order.MyOrdersController;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.order.OrderController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = {OrderController.class, MyOrdersController.class})
public class OrderBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ForQueryingOrders orderQueryService;

    @MockitoBean
    private BuyNowApplicationService buyNowApplicationService;

    @MockitoBean
    private CheckoutApplicationService checkoutApplicationService;

    @MockitoBean
    private SecurityChecks securityChecks;

    public static final String validOrderId = "01226N0640J7Q";
    public static final String notFoundOrderId = "01226N0693HDH";
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Mockito.when(securityChecks.getAuthenticatedUserId()).thenReturn(userId);
        RestAssuredMockMvc.mockMvc(
            MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockFindOrderByid();
        mockFindOrderByIdNotFound();
        mockCreateOrderWithProduct();
        mockCreateOrderWithShoppingCart();
        mockFilterOrders();
        mockFindMyOrderById();
        mockFilterMyOrders();
    }

    private void mockFindMyOrderById() {
        Mockito.when(orderQueryService.findByIdAndCustomerId(validOrderId, userId))
            .thenReturn(OrderDetailOutputTestDataBuilder.placedOrder(validOrderId).build());
    }

    private void mockFilterOrders() {
        Mockito.when(orderQueryService.filter(Mockito.any(OrderFilter.class)))
            .thenReturn(new PageImpl<>(List.of(
                OrderSummaryOutputTestDataBuilder.placedOrder(validOrderId).build()
            )));
    }

    private void mockFilterMyOrders() {
        CustomerMinimalOutput customerOutput = CustomerTestDataBuilder
            .customerMinimalOutput()
            .id(userId)
            .build();

        Mockito.when(orderQueryService.filter(
            Mockito.argThat(filter ->
                filter != null && userId.equals(filter.getCustomerId())
            )
        )).thenReturn(new PageImpl<>(List.of(
            OrderSummaryOutputTestDataBuilder
                .placedOrder(validOrderId)
                .customer(customerOutput)
                .build()
        )));
    }

    private void mockCreateOrderWithShoppingCart() {
        Mockito.when(checkoutApplicationService.checkout(Mockito.any(CheckoutInput.class)))
            .thenReturn(validOrderId);
    }

    private void mockCreateOrderWithProduct() {
        Mockito.when(buyNowApplicationService.buyNow(Mockito.any(BuyNowInput.class)))
            .thenReturn(validOrderId);
    }

    private void mockFindOrderByIdNotFound() {
        Mockito.when(orderQueryService.findById(notFoundOrderId))
            .thenThrow(new OrderNotFoundException());
    }

    private void mockFindOrderByid() {
        Mockito.when(orderQueryService.findById(validOrderId))
            .thenReturn(OrderDetailOutputTestDataBuilder.placedOrder(validOrderId).build());
    }
}
