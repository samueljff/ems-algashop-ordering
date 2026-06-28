package com.fonseca.algashop.ordering.infrastructure.beans;

import com.fonseca.algashop.ordering.core.domain.model.customer.LoyaltyPoints;
import com.fonseca.algashop.ordering.core.domain.model.order.CustomerHaveFreeShippingSpecification;
import com.fonseca.algashop.ordering.core.domain.model.order.Orders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpecificationBeanConfig {

    @Bean
    public CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification(Orders orders){
        return new CustomerHaveFreeShippingSpecification(
                orders,
                new LoyaltyPoints(200),
                2L,
                new LoyaltyPoints(2000)
        );
    }
}
