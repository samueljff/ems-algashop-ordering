package com.fonseca.algashop.ordering.domain.model.service;

import com.fonseca.algashop.ordering.domain.model.entity.Order;
import com.fonseca.algashop.ordering.domain.model.entity.PaymentMethod;
import com.fonseca.algashop.ordering.domain.model.utility.DomainService;
import com.fonseca.algashop.ordering.domain.model.valueObject.Billing;
import com.fonseca.algashop.ordering.domain.model.valueObject.Product;
import com.fonseca.algashop.ordering.domain.model.valueObject.Quantity;
import com.fonseca.algashop.ordering.domain.model.valueObject.Shipping;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.CustomerId;

@DomainService
public class BuyNowService {

    public Order buyNow(Product product,
                        CustomerId customerId,
                        Billing billing,
                        Shipping shipping,
                        Quantity quantity,
                        PaymentMethod paymentMethod
    ) {
        product.checkOutOfStock();

        Order order = Order.draft(customerId);
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        order.addItem(product, quantity);
        order.place();

        return order;
    }
}
