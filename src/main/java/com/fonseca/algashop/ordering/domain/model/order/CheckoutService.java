package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.CreditCardId;
import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.fonseca.algashop.ordering.domain.model.DomainService;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@DomainService
@RequiredArgsConstructor
public class CheckoutService {

    private final CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification;

    public Order checkout(
            Customer customer,
            ShoppingCart shoppingCart,
            Billing billing,
            Shipping shipping,
            PaymentMethod paymentMethod,
            CreditCardId creditCardId
    ) {

        if (shoppingCart.isEmpty() || shoppingCart.containsUnavailableItems()) {
            throw new ShoppingCartCantProceedToCheckoutException();
        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);
        if (haveFreeShipping(customer)){
            Shipping freeShipping = shipping.toBuilder().cost(Money.ZERO).build();
            order.changeShipping(freeShipping);
        } else {
            order.changeShipping(shipping);
        }
        order.changePaymentMethod(paymentMethod, creditCardId);

        Set<ShoppingCartItem> items = shoppingCart.items();

        for (ShoppingCartItem item : items) {
            order.addItem(new Product(item.productId(), item.name(),
                    item.price(), item.isAvailable()), item.quantity());
        }

        order.place();
        shoppingCart.empty();

        return order;
    }

    public boolean haveFreeShipping(Customer customer){
        return customerHaveFreeShippingSpecification.isSatisfiedBy(customer);
    }
}
