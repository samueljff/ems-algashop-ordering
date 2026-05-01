package com.fonseca.algashop.ordering.presentation.order;

import com.fonseca.algashop.ordering.application.checkout.BuyNowApplicationService;
import com.fonseca.algashop.ordering.application.checkout.BuyNowInput;
import com.fonseca.algashop.ordering.application.checkout.CheckoutApplicationService;
import com.fonseca.algashop.ordering.application.checkout.CheckoutInput;
import com.fonseca.algashop.ordering.application.order.query.OrderDetailOutput;
import com.fonseca.algashop.ordering.application.order.query.OrderFilter;
import com.fonseca.algashop.ordering.application.order.query.OrderQueryService;
import com.fonseca.algashop.ordering.application.order.query.OrderSummaryOutput;
import com.fonseca.algashop.ordering.presentation.PageModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderQueryService orderQueryService;
    private final CheckoutApplicationService checkoutApplicationService;
    private final BuyNowApplicationService buyNowApplicationService;

    @GetMapping("/{orderId}")
    public OrderDetailOutput findById(@PathVariable String orderId) {
        return orderQueryService.findById(orderId);
    }

    @GetMapping
    public PageModel<OrderSummaryOutput> filter(OrderFilter filter) {
        return PageModel.of(orderQueryService.filter(filter));
    }

    @PostMapping(consumes = "application/vnd.order-with-product.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailOutput createWithProduct(@Valid @RequestBody BuyNowInput input) {
        String orderId = buyNowApplicationService.buyNow(input);
        return orderQueryService.findById(orderId);
    }

    @PostMapping(consumes = "application/vnd.order-with-shopping-cart.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailOutput createWithShoppingCart(@Valid @RequestBody CheckoutInput input) {
        String orderId = checkoutApplicationService.checkout(input);
        return orderQueryService.findById(orderId);
    }

}
