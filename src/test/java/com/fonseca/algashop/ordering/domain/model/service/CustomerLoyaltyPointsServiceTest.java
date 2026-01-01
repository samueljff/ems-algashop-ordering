package com.fonseca.algashop.ordering.domain.model.service;

import com.fonseca.algashop.ordering.domain.model.entity.*;
import com.fonseca.algashop.ordering.domain.model.valueObject.LoyaltyPoints;
import com.fonseca.algashop.ordering.domain.model.valueObject.Product;
import com.fonseca.algashop.ordering.domain.model.valueObject.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerLoyaltyPointsServiceTest {

    CustomerLoyaltyPointsService customerLoyaltyPointsService = new CustomerLoyaltyPointsService();

    @Test
    public void givenValidCustomerAndOrder_WhenAddingPoints_ShouldAccumulate(){
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.READY).build();

        customerLoyaltyPointsService.addPoints(customer, order);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    public void givenValidCustomerAndOrderWithLowTotalAmount_WhenAddingPoints_ShouldNotAccumulate(){
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Product product = ProductTestDataBuilder.aProductAltRamMemory().build();

        Order order = OrderTestDataBuilder.anOrder()
                .withItems(false)
                .status(OrderStatus.DRAFT)
                .build();

        order.addItem(product, new Quantity(1));
        order.place();
        order.markAsPaid();
        order.markAsReady();

        customerLoyaltyPointsService.addPoints(customer, order);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(0));
    }
}