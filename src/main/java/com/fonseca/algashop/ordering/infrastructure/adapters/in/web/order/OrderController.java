package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.order;

import com.fonseca.algashop.ordering.core.ports.in.checkout.ForBuyingProduct;
import com.fonseca.algashop.ordering.core.ports.in.checkout.ForBuyingWithShoppingCart;
import com.fonseca.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.fonseca.algashop.ordering.core.ports.in.order.OrderFilter;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderSummaryOutput;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.PageModel;
import com.fonseca.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanReadOrders;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ForQueryingOrders orderQueryService;

    @GetMapping("/{orderId}")
    @CanReadOrders
    public OrderDetailOutput findById(@PathVariable String orderId) {
        return orderQueryService.findById(orderId);
    }

    @GetMapping
    @CanReadOrders
    public PageModel<OrderSummaryOutput> filter(OrderFilter filter) {
        return PageModel.of(orderQueryService.filter(filter));
    }
}
