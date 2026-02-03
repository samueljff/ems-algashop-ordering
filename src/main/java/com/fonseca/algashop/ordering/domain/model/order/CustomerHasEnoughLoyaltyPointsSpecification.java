package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.Specification;
import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.customer.LoyaltyPoints;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerHasEnoughLoyaltyPointsSpecification implements Specification<Customer> {

    private final LoyaltyPoints expectedLoyaltyPoints;

    @Override
    public boolean isSatisfiedBy(Customer customer) {
        return customer.loyaltyPoints().compareTo(expectedLoyaltyPoints) >= 0;
    }
}
