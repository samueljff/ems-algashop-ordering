package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.Specification;
import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.customer.LoyaltyPoints;
import lombok.RequiredArgsConstructor;

public class CustomerHaveFreeShippingSpecification implements Specification<Customer> {

    private final CustomerHasOrderedEnoughAtYearSpecification hasOrderedEnoughAtYear;
    private final CustomerHasEnoughLoyaltyPointsSpecification hasEnoughBasicLoyaltyPoints;
    private final CustomerHasEnoughLoyaltyPointsSpecification hasEnoughPremiumLoyaltyPoints;

    public CustomerHaveFreeShippingSpecification(Orders orders,
                                                 LoyaltyPoints basicLoyaltyPoints,
                                                 long salesQuantityForFreeShipping,
                                                 LoyaltyPoints premiumLoyaltyPoints) {
        this.hasOrderedEnoughAtYear = new CustomerHasOrderedEnoughAtYearSpecification(orders,
                salesQuantityForFreeShipping);

        this.hasEnoughBasicLoyaltyPoints = new CustomerHasEnoughLoyaltyPointsSpecification(basicLoyaltyPoints);
        this.hasEnoughPremiumLoyaltyPoints = new CustomerHasEnoughLoyaltyPointsSpecification(premiumLoyaltyPoints);
    }

    @Override
    public boolean isSatisfiedBy(Customer customer) {
        return hasEnoughBasicLoyaltyPoints
                .and(hasOrderedEnoughAtYear)
                .or(hasEnoughPremiumLoyaltyPoints)
                .isSatisfiedBy(customer);
    }
}
