package com.fonseca.algashop.ordering.domain.model.service;

import com.fonseca.algashop.ordering.domain.model.entity.ShoppingCart;
import com.fonseca.algashop.ordering.domain.model.exceptions.CustomerAlreadyHaveShoppingCartException;
import com.fonseca.algashop.ordering.domain.model.exceptions.CustomerNotFoundException;
import com.fonseca.algashop.ordering.domain.model.repository.Customers;
import com.fonseca.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.fonseca.algashop.ordering.domain.model.utility.DomainService;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.CustomerId;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ShoppingService {

    private final ShoppingCarts shoppingCarts;
    private final Customers customers;

    public ShoppingCart startShopping(CustomerId customerId) {
        if (!customers.exists(customerId)) {
            throw new CustomerNotFoundException();
        }

        if (shoppingCarts.ofCustomer(customerId).isPresent()) {
            throw new CustomerAlreadyHaveShoppingCartException();
        }

        return ShoppingCart.startShopping(customerId);
    }
}
