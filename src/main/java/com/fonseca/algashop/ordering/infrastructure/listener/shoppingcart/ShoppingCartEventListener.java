package com.fonseca.algashop.ordering.infrastructure.listener.shoppingcart;

import com.fonseca.algashop.ordering.domain.model.shoppingcart.events.ShoppingCartCreatedEvent;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.events.ShoppingCartEmptiedEvent;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.events.ShoppingCartItemAddedEvent;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.events.ShoppingCartItemRemovedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartEventListener {

    @EventListener
    public void listen(ShoppingCartCreatedEvent event) {

    }

    @EventListener
    public void listen(ShoppingCartEmptiedEvent event) {

    }

    @EventListener
    public void listen(ShoppingCartItemAddedEvent event) {

    }

    @EventListener
    public void listen(ShoppingCartItemRemovedEvent event) {

    }
}
