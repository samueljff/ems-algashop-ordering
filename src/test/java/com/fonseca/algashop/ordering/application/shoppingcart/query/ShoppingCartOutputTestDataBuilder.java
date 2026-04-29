package com.fonseca.algashop.ordering.application.shoppingcart.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ShoppingCartOutputTestDataBuilder {
    public static ShoppingCartOutput.ShoppingCartOutputBuilder aShoppingCart() {
        return ShoppingCartOutput.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .totalItems(2)
                .totalAmount(new BigDecimal(1800))
                .items(List.of(
                        existingItem().build(),
                        existingItemAlt().build()
                ));
    }

    public static ShoppingCartItemOutput.ShoppingCartItemOutputBuilder existingItem() {
        return ShoppingCartItemOutput.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .price(new BigDecimal(600))
                .quantity(2)
                .totalAmount(new BigDecimal(1200))
                .available(true)
                .name("Desktop");
    }

    public static ShoppingCartItemOutput.ShoppingCartItemOutputBuilder existingItemAlt() {
        return ShoppingCartItemOutput.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .price(new BigDecimal(300))
                .quantity(2)
                .totalAmount(new BigDecimal(600))
                .available(true)
                .name("Monitor");
    }
}