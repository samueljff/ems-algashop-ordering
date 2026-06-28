package com.fonseca.algashop.ordering.core.domain.model.shoppingcart.events;

import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartId;

import java.time.OffsetDateTime;

public record ShoppingCartEmptiedEvent(ShoppingCartId shoppingCartId, CustomerId customerId, OffsetDateTime emptiedAt) {
}
