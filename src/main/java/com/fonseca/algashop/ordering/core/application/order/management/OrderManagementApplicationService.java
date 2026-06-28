package com.fonseca.algashop.ordering.core.application.order.management;

import com.fonseca.algashop.ordering.core.domain.model.order.Order;
import com.fonseca.algashop.ordering.core.domain.model.order.OrderId;
import com.fonseca.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService {

    private final Orders orders;

    @Transactional
    public void cancel(Long rawOrderId){
        Objects.requireNonNull(rawOrderId);
        OrderId orderId = new OrderId(rawOrderId);
        Order order = orders.ofId(orderId).orElseThrow(OrderNotFoundException::new);

        order.cancel();

        orders.add(order);
    }

    @Transactional
    public void markAsPaid(Long rawOrderId){
        Objects.requireNonNull(rawOrderId);
        OrderId orderId = new OrderId(rawOrderId);
        Order order = orders.ofId(orderId).orElseThrow(OrderNotFoundException::new);

        order.markAsPaid();

        orders.add(order);
    }

    @Transactional
    public void markAsReady(Long rawOrderId){
        Objects.requireNonNull(rawOrderId);
        OrderId orderId = new OrderId(rawOrderId);
        Order order = orders.ofId(orderId).orElseThrow(OrderNotFoundException::new);

        order.markAsReady();

        orders.add(order);
    }
}
