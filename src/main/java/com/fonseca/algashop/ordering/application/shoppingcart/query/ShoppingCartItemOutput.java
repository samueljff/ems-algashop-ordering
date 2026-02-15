package com.fonseca.algashop.ordering.application.shoppingcart.query;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ShoppingCartItemOutput {
    private UUID id;
    private UUID productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Boolean available;
}
