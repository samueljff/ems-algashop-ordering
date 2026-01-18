package com.fonseca.algashop.ordering.application.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyNowInput {
    private ShippingInput shipping;
    private BillingData billing;
    private UUID productId;
    private UUID customerId;
    private Integer quantity;
    private String paymentMethod;
}
