package com.fonseca.algashop.ordering.contract.base;

import com.fonseca.algashop.ordering.core.application.checkout.BuyNowApplicationService;
import com.fonseca.algashop.ordering.core.application.checkout.BuyNowInput;
import com.fonseca.algashop.ordering.core.application.checkout.CheckoutApplicationService;
import com.fonseca.algashop.ordering.core.application.checkout.CheckoutInput;
import com.fonseca.algashop.ordering.core.application.order.query.OrderDetailOutputTestDataBuilder;
import com.fonseca.algashop.ordering.core.application.order.query.OrderFilter;
import com.fonseca.algashop.ordering.core.application.order.query.OrderQueryService;
import com.fonseca.algashop.ordering.core.application.order.query.OrderSummaryOutputTestDataBuilder;
import com.fonseca.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.fonseca.algashop.ordering.presentation.order.OrderController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

@WebMvcTest(controllers = OrderController.class)
public class OrderBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private OrderQueryService orderQueryService;

    @MockitoBean
    private BuyNowApplicationService buyNowApplicationService;

    @MockitoBean
    private CheckoutApplicationService checkoutApplicationService;

    public static final String validOrderId = "01226N0640J7Q";
    public static final String notFoundOrderId = "01226N0693HDH";

    @BeforeEach
    void setUp() {
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
    }

    private void mockFilterOrders() {
        Mockito.when(orderQueryService.filter(Mockito.any(OrderFilter.class)))
            .thenReturn(new PageImpl<>(List.of(
                OrderSummaryOutputTestDataBuilder.placedOrder(validOrderId).build()
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
