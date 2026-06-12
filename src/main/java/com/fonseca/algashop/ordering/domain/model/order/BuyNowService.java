package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.CreditCardId;
import com.fonseca.algashop.ordering.domain.model.DomainService;
import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import lombok.RequiredArgsConstructor;

import java.time.Year;

@DomainService
@RequiredArgsConstructor
public class BuyNowService {

    private final CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification;

    public Order buyNow(Product product,
                        Customer customer,
                        Billing billing,
                        Shipping shipping,
                        Quantity quantity,
                        PaymentMethod paymentMethod,
                        CreditCardId creditCardId) {
        product.checkOutOfStock();

        Order order = Order.draft(customer.id());
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod, creditCardId);
        order.addItem(product, quantity);

        if (haveFreeShipping(customer)){
            Shipping freeShipping = shipping.toBuilder().cost(Money.ZERO).build();
            order.changeShipping(freeShipping);
        } else {
            order.changeShipping(shipping);
        }
        order.place();

        return order;
    }

    private boolean haveFreeShipping(Customer customer) {
        return customerHaveFreeShippingSpecification.isSatisfiedBy(customer);
    }
}
