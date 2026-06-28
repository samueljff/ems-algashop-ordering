package com.fonseca.algashop.ordering.core.application.customer.loyaltypoints;

import com.fonseca.algashop.ordering.core.domain.model.customer.*;
import com.fonseca.algashop.ordering.core.domain.model.order.Order;
import com.fonseca.algashop.ordering.core.domain.model.order.OrderId;
import com.fonseca.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerLoyaltyPointsApplicationService {

    private final CustomerLoyaltyPointsService customerLoyaltyPointsService;
    private final Customers customers;
    private final Orders orders;

    @Transactional
    public void addLoyaltyPoints(UUID rawCustomerId, String rawOrderId) {
        CustomerId customerId = new CustomerId(rawCustomerId);
        OrderId orderId = new OrderId(rawOrderId);

        Order order = orders.ofId(orderId).orElseThrow(OrderNotFoundException::new);
        Customer customer = customers.ofId(customerId).orElseThrow(CustomerNotFoundException::new);

        customerLoyaltyPointsService.addPoints(customer, order);

        customers.add(customer);
    }
}
