package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.DomainException;

import static com.fonseca.algashop.ordering.domain.model.ErrorMessages.ERROR_ORDER_STATUS_CANNOT_BE_CHANGE;

public class OrderStatusCannotBeChangedException extends DomainException {
    public OrderStatusCannotBeChangedException(OrderId id, OrderStatus status, OrderStatus newStatus) {
        super(String.format(ERROR_ORDER_STATUS_CANNOT_BE_CHANGE,  id, status, newStatus));
    }
}
