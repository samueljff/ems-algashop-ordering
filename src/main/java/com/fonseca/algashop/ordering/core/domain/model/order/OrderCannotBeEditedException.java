package com.fonseca.algashop.ordering.core.domain.model.order;

import com.fonseca.algashop.ordering.core.domain.model.DomainException;

import static com.fonseca.algashop.ordering.core.domain.model.ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException {
    public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
        super(String.format(ERROR_ORDER_CANNOT_BE_EDITED, id, status));
    }
}
