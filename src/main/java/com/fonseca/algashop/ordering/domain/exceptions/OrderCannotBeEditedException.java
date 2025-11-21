package com.fonseca.algashop.ordering.domain.exceptions;

import com.fonseca.algashop.ordering.domain.entity.OrderStatus;
import com.fonseca.algashop.ordering.domain.valueObject.id.OrderId;

import static com.fonseca.algashop.ordering.domain.exceptions.ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException {
    public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
        super(String.format(ERROR_ORDER_CANNOT_BE_EDITED, id, status));
    }
}
