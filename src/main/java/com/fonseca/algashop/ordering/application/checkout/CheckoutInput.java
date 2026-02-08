package com.fonseca.algashop.ordering.application.checkout;

import com.fonseca.algashop.ordering.application.order.query.BillingData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutInput {
    private UUID shoppingCartId;
    private String paymentMethod;
    private ShippingInput shipping;
    private BillingData billing;
}
