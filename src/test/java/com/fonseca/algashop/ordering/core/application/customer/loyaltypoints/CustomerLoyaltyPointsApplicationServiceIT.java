package com.fonseca.algashop.ordering.core.application.customer.loyaltypoints;

import com.fonseca.algashop.ordering.core.application.AbstractApplicationIT;
import com.fonseca.algashop.ordering.core.domain.model.commons.Money;
import com.fonseca.algashop.ordering.core.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.core.domain.model.customer.*;
import com.fonseca.algashop.ordering.core.domain.model.order.*;
import com.fonseca.algashop.ordering.core.domain.model.product.Product;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import com.fonseca.algashop.ordering.infrastructure.listener.customer.CustomerEventListener;
import io.hypersistence.tsid.TSID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

class CustomerLoyaltyPointsApplicationServiceIT extends AbstractApplicationIT {

    @Autowired
    private CustomerLoyaltyPointsApplicationService service;

    @Autowired
    private Customers customers;

    @Autowired
    private Orders orders;

    @MockitoBean
    CustomerEventListener customerEventListener;

    @Test
    void givenReadyOrderWithValueAboveLimit_whenAddLoyaltyPoints_thenPointsAreAdded() {
        // Given
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
            .customerId(customer.id())
            .status(OrderStatus.DRAFT)
            .withItems(false)
            .build();
        orders.add(order);

        Product product = ProductTestDataBuilder.aProduct().price(new Money("2000")).build();

        order.addItem(product, new Quantity(2));
        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        // When
        service.addLoyaltyPoints(customer.id().value(), order.id().value().toString());

        // Then
        Customer updatedCustomer = customers.ofId(customer.id()).orElseThrow();
        Assertions.assertThat(updatedCustomer.id()).isEqualTo(customer.id());
        Assertions.assertThat(updatedCustomer).isNotNull();
        Assertions.assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(20));
    }

    @Test
    void givenNonExistCustomerId_whenAddLoyaltyPoints_thenThrowCustomerNotFoundException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
            .customerId(customer.id())
            .status(OrderStatus.DRAFT)
            .build();
        orders.add(order);

        orders.add(order);

        Assertions.assertThatThrownBy(() ->
            service.addLoyaltyPoints(UUID.randomUUID(), order.id().value().toString())
        ).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void givenNonExistOrderId_whenAddLoyaltyPoints_thenThrowOrderNotFoundException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        String nonExistingOrderId = TSID.fast().toString();
        Assertions.assertThatThrownBy(() ->
            service.addLoyaltyPoints(customer.id().value(), nonExistingOrderId)
        ).isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void givenArchivedCustomer_whenAddLoyaltyPoints_thenThrowCustomerArchivedException() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customer.archive();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
            .customerId(customer.id())
            .status(OrderStatus.DRAFT)
            .build();
        orders.add(order);

        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        Assertions.assertThatThrownBy(() ->
            service.addLoyaltyPoints(customer.id().value(), order.id().value().toString())
        ).isInstanceOf(CustomerArchivedException.class);
    }

    @Test
    void givenOrderFromAnotherCustomer_whenAddLoyaltyPoints_thenThrowOrderNotBelongsToCustomerException() {
        Customer customerA = CustomerTestDataBuilder.brandNewCustomer().build();
        Customer customerB = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customerA);
        customers.add(customerB);

        Order order = OrderTestDataBuilder.anOrder()
            .customerId(customerB.id())
            .status(OrderStatus.DRAFT)
            .build();
        orders.add(order);

        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        Assertions.assertThatThrownBy(() ->
            service.addLoyaltyPoints(customerA.id().value(), order.id().value().toString())
        ).isInstanceOf(OrderNotBeLongsToCustomerException.class);
    }

    @Test
    void givenOrderNotReady_whenAddLoyaltyPoints_thenThrowCantAddLoyaltyPointsOrderIsNotReady() {
        // Given
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
            .customerId(customer.id())
            .status(OrderStatus.DRAFT)
            .build();
        orders.add(order);

        orders.add(order);

        // When / Then
        Assertions.assertThatThrownBy(() ->
            service.addLoyaltyPoints(customer.id().value(), order.id().value().toString())
        ).isInstanceOf(CanAddLoyaltyPointsOrderIsNotReady.class);
    }

    @Test
    void givenOrderBelowMinimumValue_whenAddLoyaltyPoints_thenNoPointsAreAdded() {
        // Given
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
            .customerId(customer.id())
            .status(OrderStatus.DRAFT)
            .withItems(false)
            .build();
        orders.add(order);

        Product product = ProductTestDataBuilder.aProduct()
            .price(new Money("900"))
            .build();

        order.addItem(product, new Quantity(1));
        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        // When
        service.addLoyaltyPoints(customer.id().value(), order.id().value().toString());

        // Then
        Customer updatedCustomer = customers.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(updatedCustomer.loyaltyPoints())
            .isEqualTo(LoyaltyPoints.ZERO);
    }
}