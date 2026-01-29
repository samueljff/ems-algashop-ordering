package com.fonseca.algashop.ordering.infrastructure.listener.order;

import com.fonseca.algashop.ordering.domain.model.order.notification.OrderCanceledEvent;
import com.fonseca.algashop.ordering.domain.model.order.notification.OrderPaidEvent;
import com.fonseca.algashop.ordering.domain.model.order.notification.OrderPlacedEvent;
import com.fonseca.algashop.ordering.domain.model.order.notification.OrderReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @EventListener
    public void listen(OrderPlacedEvent event) {

    }

    @EventListener
    public void listen(OrderPaidEvent event) {

    }

    @EventListener
    public void listen(OrderReadyEvent event) {

    }

    @EventListener
    public void listen(OrderCanceledEvent event) {

    }
}
