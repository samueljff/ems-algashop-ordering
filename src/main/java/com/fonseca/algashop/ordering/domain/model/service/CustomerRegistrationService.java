package com.fonseca.algashop.ordering.domain.model.service;

import com.fonseca.algashop.ordering.domain.model.entity.Customer;
import com.fonseca.algashop.ordering.domain.model.exceptions.CustomerEmailIsInUseException;
import com.fonseca.algashop.ordering.domain.model.repository.Customers;
import com.fonseca.algashop.ordering.domain.model.utility.DomainService;
import com.fonseca.algashop.ordering.domain.model.valueObject.*;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.CustomerId;
import lombok.AllArgsConstructor;

@DomainService
@AllArgsConstructor
public class CustomerRegistrationService {

    private final Customers customers;

    public Customer register(
            FullName fullName, BirthDate birthDate, Email email,
            Phone phone, Document document, Boolean promotionNotificationsAllowed,
            Address address
    ) {
        Customer customer = Customer.brandNew()
                .fullName(fullName)
                .birthDate(birthDate)
                .email(email)
                .phone(phone)
                .document(document)
                .promotionNotificationsAllowed(promotionNotificationsAllowed)
                .address(address)
                .build();

        verifyEmailUniqueness(customer.email(), customer.id());

        return customer;
    }

    public void changeEmail(Customer customer, Email newEmail){
        verifyEmailUniqueness(newEmail, customer.id());
        customer.changeEmail(newEmail);
    }

    private void verifyEmailUniqueness(Email email, CustomerId customerId) {
        if (!customers.isEmailUnique(email, customerId)){
            throw new CustomerEmailIsInUseException();
        }
    }
}
