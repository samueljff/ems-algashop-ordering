package com.fonseca.algashop.ordering.domain.entity;

import com.fonseca.algashop.ordering.domain.valueObject.Quantity;
import com.fonseca.algashop.ordering.domain.valueObject.id.OrderId;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    public void shouldGenerate() {
        OrderItem.brandNew()
                .product(ProductTestDataBuilder.aProduct().build())
                .quantity(new Quantity(1))
                .orderId(new OrderId())
                .build();
    }
}