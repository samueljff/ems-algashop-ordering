package com.fonseca.algashop.ordering.application.shoppingcart.query;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ShoppingCartOutput {
    private UUID id;
    private UUID customerId;
    private Integer totalItems;
    private BigDecimal totalAmount;
    private List<ShoppingCartItemOutput> items = new ArrayList<>();
}
