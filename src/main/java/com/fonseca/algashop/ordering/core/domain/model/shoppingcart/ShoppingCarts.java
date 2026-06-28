package com.fonseca.algashop.ordering.core.domain.model.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.RemoveCapableRepository;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerId;

import java.util.Optional;

public interface ShoppingCarts extends RemoveCapableRepository<ShoppingCart, ShoppingCartId> {
    Optional<ShoppingCart> ofCustomer(CustomerId customerId);
}
