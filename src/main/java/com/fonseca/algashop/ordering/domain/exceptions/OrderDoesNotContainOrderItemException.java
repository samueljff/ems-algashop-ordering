package com.fonseca.algashop.ordering.domain.exceptions;

import com.fonseca.algashop.ordering.domain.valueobjet.id.OrderId;
import com.fonseca.algashop.ordering.domain.valueobjet.id.OrderItemId;

public class OrderDoesNotContainOrderItemException extends DomainException {
    public OrderDoesNotContainOrderItemException(OrderId id, OrderItemId orderItemId) {
        super(String.format(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM, id, orderItemId));
    }
}