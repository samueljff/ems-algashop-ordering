package com.fonseca.algashop.ordering.domain.factory;

import com.fonseca.algashop.ordering.domain.entity.Order;
import com.fonseca.algashop.ordering.domain.entity.PaymentMethod;
import com.fonseca.algashop.ordering.domain.valueObject.Billing;
import com.fonseca.algashop.ordering.domain.valueObject.Product;
import com.fonseca.algashop.ordering.domain.valueObject.Quantity;
import com.fonseca.algashop.ordering.domain.valueObject.Shipping;
import com.fonseca.algashop.ordering.domain.valueObject.id.CustomerId;

import java.util.Objects;

public class OrderFactory {

    private OrderFactory() {
    }

    public static Order filled(
            CustomerId customerId,
            Shipping shipping,
            Billing billing,
            PaymentMethod paymentMethod,
            Product product,
            Quantity productQuantity
    ) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(billing);
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(product);
        Objects.requireNonNull(productQuantity);

        Order order = Order.draft(customerId);

        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product, productQuantity);

        return order;
    }
}
