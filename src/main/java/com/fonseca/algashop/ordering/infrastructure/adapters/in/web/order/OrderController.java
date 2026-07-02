package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.order;

import com.fonseca.algashop.ordering.core.application.checkout.BuyNowApplicationService;
import com.fonseca.algashop.ordering.core.ports.in.checkout.BuyNowInput;
import com.fonseca.algashop.ordering.core.application.checkout.CheckoutApplicationService;
import com.fonseca.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.fonseca.algashop.ordering.core.ports.in.order.OrderFilter;
import com.fonseca.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderSummaryOutput;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.fonseca.algashop.ordering.presentation.PageModel;
import com.fonseca.algashop.ordering.presentation.UnprocessableEntityException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ForQueryingOrders orderQueryService;
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
        String orderId;
        try {
            orderId = buyNowApplicationService.buyNow(input);
        } catch (CustomerNotFoundException | ProductNotFoundException e){
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
        return orderQueryService.findById(orderId);
    }

    @PostMapping(consumes = "application/vnd.order-with-shopping-cart.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailOutput createWithShoppingCart(@Valid @RequestBody CheckoutInput input) {
        String orderId;
        try {
            orderId = checkoutApplicationService.checkout(input);
        } catch (CustomerNotFoundException | ShoppingCartNotFoundException e){
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
        return orderQueryService.findById(orderId);
    }

}
