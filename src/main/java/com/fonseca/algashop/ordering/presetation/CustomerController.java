package com.fonseca.algashop.ordering.presetation;

import com.fonseca.algashop.ordering.application.commons.AddressData;
import com.fonseca.algashop.ordering.application.customer.management.CustomerInput;
import com.fonseca.algashop.ordering.application.customer.query.CustomerOutput;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOutput create(@RequestBody CustomerInput input){

        AddressData address = input.getAddress();
        return CustomerOutput.builder()
                .id(UUID.randomUUID())
                .phone(input.getPhone())
                .email(input.getEmail())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .birthDate(input.getBirthDate())
                .document(input.getDocument())
                .promotionNotificationsAllowed(input.getPromotionNotificationsAllowed())
                .registeredAt(OffsetDateTime.now())
                .archived(false)
                .loyaltyPoints(0)
                .address(address)
                .build();
    }
}
