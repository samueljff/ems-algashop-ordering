package com.fonseca.algashop.ordering.infrastructure.notification.customer;

import com.fonseca.algashop.ordering.core.application.customer.notification.CustomerNotificationApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerNotificationServiceFakeImpl implements CustomerNotificationApplicationService {

    @Override
    public void notifyNewRegistration(NotifyNewRegistrationInput input) {

        log.info("Welcome {} ", input.firstName());
        log.info("User your email to access your account {} ", input.email());
    }
}
