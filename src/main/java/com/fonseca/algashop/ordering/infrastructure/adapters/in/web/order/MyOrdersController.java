package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.order;

import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.fonseca.algashop.ordering.core.ports.in.checkout.BuyNowInput;
import com.fonseca.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import com.fonseca.algashop.ordering.core.ports.in.checkout.ForBuyingProduct;
import com.fonseca.algashop.ordering.core.ports.in.checkout.ForBuyingWithShoppingCart;
import com.fonseca.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.fonseca.algashop.ordering.core.ports.in.order.OrderFilter;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderSummaryOutput;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.PageModel;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.UnprocessableEntityException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.fonseca.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanReadMyOrders;
import static com.fonseca.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanWriteMyOrders;

@RestController
@RequestMapping(path = "/api/v1/customers/me/orders")
@RequiredArgsConstructor
public class MyOrdersController {

    private final ForQueryingOrders orderQueryService;
    private final ForBuyingWithShoppingCart forBuyingWithShoppingCart;
    private final ForBuyingProduct forBuyingProduct;
    private final SecurityChecks  securityChecks;

    @GetMapping("/{orderId}")
    @CanReadMyOrders
    public OrderDetailOutput findById(@PathVariable String orderId) {
        return orderQueryService.findByIdAndCustomerId(orderId, securityChecks.getAuthenticatedUserId());
    }

    @GetMapping
    @CanReadMyOrders
    public PageModel<OrderSummaryOutput> filter(OrderFilter filter) {
        filter.setCustomerId(securityChecks.getAuthenticatedUserId());
        return PageModel.of(orderQueryService.filter(filter));
    }

    @PostMapping(consumes = "application/vnd.order-with-product.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    @CanWriteMyOrders
    public OrderDetailOutput createWithProduct(@Valid @RequestBody BuyNowInput input) {
        input.setCustomerId(securityChecks.getAuthenticatedUserId());
        String orderId;
        try {
            orderId = forBuyingProduct.buyNow(input);
        } catch (CustomerNotFoundException | ProductNotFoundException e){
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
        return orderQueryService.findById(orderId);
    }

    @PostMapping(consumes = "application/vnd.order-with-shopping-cart.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    @CanWriteMyOrders
    public OrderDetailOutput createWithShoppingCart(@Valid @RequestBody CheckoutInput input) {
        input.setCustomerId(securityChecks.getAuthenticatedUserId());
        String orderId;
        try {
            orderId = forBuyingWithShoppingCart.checkout(input);
        } catch (CustomerNotFoundException | ShoppingCartNotFoundException e){
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
        return orderQueryService.findById(orderId);
    }

}
