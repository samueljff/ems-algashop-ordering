package com.fonseca.algashop.ordering.infrastructure.listener.customer;

import com.fonseca.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.fonseca.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService.*;

@Component
@Log
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerNotificationApplicationService customerNotificationService;

    @EventListener
    public void listen(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent listen 1");
        NotifyNewRegistrationInput input = new NotifyNewRegistrationInput(
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
}
