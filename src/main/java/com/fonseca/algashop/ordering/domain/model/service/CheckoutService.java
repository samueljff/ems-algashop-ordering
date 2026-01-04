package com.fonseca.algashop.ordering.domain.model.service;

import com.fonseca.algashop.ordering.domain.model.entity.Order;
import com.fonseca.algashop.ordering.domain.model.entity.PaymentMethod;
import com.fonseca.algashop.ordering.domain.model.entity.ShoppingCart;
import com.fonseca.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.fonseca.algashop.ordering.domain.model.exceptions.ShoppingCartCantProceedToCheckoutException;
import com.fonseca.algashop.ordering.domain.model.utility.DomainService;
import com.fonseca.algashop.ordering.domain.model.valueObject.Billing;
import com.fonseca.algashop.ordering.domain.model.valueObject.Product;
import com.fonseca.algashop.ordering.domain.model.valueObject.Shipping;

import java.util.Set;

@DomainService
public class CheckoutService {

    public Order checkout(
            ShoppingCart shoppingCart,
            Billing billing,
            Shipping shipping,
            PaymentMethod paymentMethod
    ) {

        if (shoppingCart.isEmpty() || shoppingCart.containsUnavailableItems()) {
            throw new ShoppingCartCantProceedToCheckoutException();
        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        Set<ShoppingCartItem> items = shoppingCart.items();

        for (ShoppingCartItem item : items) {
            order.addItem(new Product(item.productId(), item.name(),
                    item.price(), item.isAvailable()), item.quantity());
        }

        order.place();
        shoppingCart.empty();

        return order;
    }
}
