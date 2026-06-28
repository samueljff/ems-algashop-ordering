package com.fonseca.algashop.ordering.infrastructure.listener.customer;

import com.fonseca.algashop.ordering.core.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.fonseca.algashop.ordering.core.application.customer.notification.CustomerNotificationApplicationService;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerArchivedEvent;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerRegisteredEvent;
import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerNotificationApplicationService customerNotificationService;
    private final CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

    @EventListener
    public void listen(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent listen 1");
        CustomerNotificationApplicationService.NotifyNewRegistrationInput input = new CustomerNotificationApplicationService.NotifyNewRegistrationInput(
                event.customerId().value(),
                event.fullName().firstName(),
                event.email().value()
        );
        customerNotificationService.notifyNewRegistration(input);
    }

    @EventListener
    public void listen(CustomerArchivedEvent event){
        log.info("CustomerArchivedEvent listen 1");
    }

    @EventListener
    public void listen(OrderReadyEvent event){
        customerLoyaltyPointsApplicationService.addLoyaltyPoints(
                event.customerId().value(),
                event.orderId().toString()
        );
    }
}
