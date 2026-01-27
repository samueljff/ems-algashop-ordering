package com.fonseca.algashop.ordering.infrastructure.notification.customer;

import com.fonseca.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.fonseca.algashop.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerNotificationServiceFakeImpl implements CustomerNotificationService {

    private final Customers customers;

    @Override
    public void notifyNewRegistration(UUID customerId) {

        Customer customer = customers.ofId(new CustomerId(customerId)).orElseThrow(() -> new CustomerNotFoundException());

        log.info("Welcome {} ", customer.fullName().firstName());
        log.info("User your email to access your account {} ", customer.email());
    }
}
