package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.customer;

import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerFilter;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerSummaryOutput;
import com.fonseca.algashop.ordering.core.ports.in.customer.ForQueryingCustomers;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.PageModel;
import com.fonseca.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanReadCustomers;
import com.fonseca.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanReadShoppingCarts;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ForQueryingCustomers forQueryingCustomers;
    private final ForQueryingShoppingCarts forQueryingShoppingCarts;

    @GetMapping
    @CanReadCustomers
    public PageModel<CustomerSummaryOutput> findAll(CustomerFilter customerFilter) {
        return PageModel.of(forQueryingCustomers.filter(customerFilter));
    }

    @GetMapping("/{customerId}")
    @CanReadCustomers
    public CustomerOutput findById(@PathVariable UUID customerId) {
        return forQueryingCustomers.findById(customerId);
    }

    @GetMapping("/{customerId}/shopping-cart")
    @CanReadShoppingCarts
    public ShoppingCartOutput findShoppingCartByCustomerId(@PathVariable UUID customerId) {
        return forQueryingShoppingCarts.findByCustomerId(customerId);
    }
}
