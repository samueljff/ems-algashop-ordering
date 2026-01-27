package com.fonseca.algashop.ordering.infrastructure.listener.customer;

import com.fonseca.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log
public class CustomerEventListener {

    @EventListener
    public void listen(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent listen 1");
    }

    @EventListener
    public void listenSecondary(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent listen 2");
    }

    @EventListener
    public void listen(CustomerArchivedEvent event){
        log.info("CustomerArchivedEvent listen 1");
    }
}
