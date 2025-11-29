package com.fonseca.algashop.ordering.domain.model.exceptions;

import com.fonseca.algashop.ordering.domain.model.valueObject.id.OrderId;

public class OrderInvalidShippingDeliveryDateException extends DomainException {
    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ErrorMessages.ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST, id));
    }
}
