package com.fonseca.algashop.ordering.domain.exceptions;

import com.fonseca.algashop.ordering.domain.valueobjet.id.OrderId;

import static com.fonseca.algashop.ordering.domain.exceptions.ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED_HAS_NOT_ITEMS;

public class OrderCannotBePlacedException extends DomainException {

    public OrderCannotBePlacedException(OrderId id) {
        super(String.format(ERROR_ORDER_CANNOT_BE_PLACED_HAS_NOT_ITEMS, id));
    }
}
