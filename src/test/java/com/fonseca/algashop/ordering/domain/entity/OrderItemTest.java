package com.fonseca.algashop.ordering.domain.entity;

import com.fonseca.algashop.ordering.domain.valueobjet.Money;
import com.fonseca.algashop.ordering.domain.valueobjet.ProductName;
import com.fonseca.algashop.ordering.domain.valueobjet.Quantity;
import com.fonseca.algashop.ordering.domain.valueobjet.id.OrderId;
import com.fonseca.algashop.ordering.domain.valueobjet.id.ProductId;
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