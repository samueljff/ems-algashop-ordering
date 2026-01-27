package com.fonseca.algashop.ordering.infrastructure.listener.customer;

import com.fonseca.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerNotificationService customerNotificationService;

    @EventListener
    public void listen(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent listen 1");
        customerNotificationService.notifyNewRegistration(event.customerId().value());
    }

    @EventListener
    public void listen(CustomerArchivedEvent event){
        log.info("CustomerArchivedEvent listen 1");
    }
}
