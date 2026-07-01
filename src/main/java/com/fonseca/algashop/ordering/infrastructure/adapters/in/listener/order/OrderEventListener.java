package com.fonseca.algashop.ordering.infrastructure.adapters.in.listener.order;

import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderCanceledEvent;
import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderPaidEvent;
import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderPlacedEvent;
import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderReadyEvent;
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
