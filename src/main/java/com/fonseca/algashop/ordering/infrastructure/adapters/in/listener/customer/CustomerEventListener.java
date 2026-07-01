package com.fonseca.algashop.ordering.infrastructure.adapters.in.listener.customer;

import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerArchivedEvent;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerRegisteredEvent;
import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderReadyEvent;
import com.fonseca.algashop.ordering.core.ports.in.customer.ForAddingLoyaltyPoints;
import com.fonseca.algashop.ordering.core.ports.in.customer.ForConfirmCustomerRegistration;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log
@RequiredArgsConstructor
public class CustomerEventListener {

    private final ForConfirmCustomerRegistration confirmCustomerRegistration;
    private final ForAddingLoyaltyPoints forAddingLoyaltyPoints;

    @EventListener
    public void listen(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent listen 1");
        confirmCustomerRegistration.confirm(event.customerId().value());
    }

    @EventListener
    public void listen(CustomerArchivedEvent event){
        log.info("CustomerArchivedEvent listen 1");
    }

    @EventListener
    public void listen(OrderReadyEvent event){
        forAddingLoyaltyPoints.addLoyaltyPoints(
                event.customerId().value(),
                event.orderId().toString()
        );
    }
}
