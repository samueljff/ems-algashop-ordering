package com.fonseca.algashop.ordering.domain.model.customer;

import com.fonseca.algashop.ordering.domain.model.order.Order;
import com.fonseca.algashop.ordering.domain.model.order.OrderNotBeLongsToCustomerException;
import com.fonseca.algashop.ordering.domain.model.DomainService;
import com.fonseca.algashop.ordering.domain.model.commons.Money;

import java.util.Objects;

@DomainService
public class CustomerLoyaltyPointsService {

    private static final LoyaltyPoints basePoints = new LoyaltyPoints(5);
    private static final Money expectedAmountToGivenPoints = new Money("1000");

    public void addPoints(Customer customer, Order order) {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(order);

        if (!customer.id().equals(order.customerId())) {
            throw new OrderNotBeLongsToCustomerException();
        }

        if (!order.isReady()){
            throw new CanAddLoyaltyPointsOrderIsNotReady();
        }

        customer.addLoyaltyPoints(calculatePoints(order));
    }

    private LoyaltyPoints calculatePoints(Order order) {
        if (shouldGivePointsByAmount(order.totalAmount())){
            Money result = order.totalAmount().divide(expectedAmountToGivenPoints);
            return new LoyaltyPoints(result.value().intValue() * basePoints.value());
        }
        return LoyaltyPoints.ZERO;
    }

    private boolean shouldGivePointsByAmount(Money amount) {
        return  amount.compareTo(expectedAmountToGivenPoints) >= 0;
    }
}
