package com.fonseca.algashop.ordering.domain.exceptions;

import com.fonseca.algashop.ordering.domain.entity.OrderStatus;
import com.fonseca.algashop.ordering.domain.valueobjet.id.OrderId;

import static com.fonseca.algashop.ordering.domain.exceptions.ErrorMessages.ERROR_ORDER_STATUS_CANNOT_BE_CHANGE;

public class OrderStatusCannotBeChangedException extends DomainException{
    public OrderStatusCannotBeChangedException(OrderId id, OrderStatus status, OrderStatus newStatus) {
        super(String.format(ERROR_ORDER_STATUS_CANNOT_BE_CHANGE,  id, status, newStatus));
    }
}
