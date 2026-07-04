package com.fonseca.algashop.ordering.core.application.order;

import com.fonseca.algashop.ordering.core.ports.out.order.CustomerMinimalOutput;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderSummaryOutput;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderSummaryOutputTestDataBuilder {

    public static OrderSummaryOutput.OrderSummaryOutputBuilder placedOrder(String orderId) {
        return OrderSummaryOutput.builder()
            .id(orderId)
            .customer(CustomerMinimalOutput.builder()
                .id(new CustomerId().value())
                .firstName("Alice")
                .lastName("Cooper")
                .document("67890")
                .email("alice.cooper@email.com")
                .phone("1199887766")
                .build())
            .totalItems(3)
            .totalAmount(new BigDecimal("89.97"))
            .placedAt(OffsetDateTime.now())
            .paidAt(null)
            .canceledAt(null)
            .readyAt(null)
            .status("PLACED")
            .paymentMethod("CREDIT_CARD");
    }

}
