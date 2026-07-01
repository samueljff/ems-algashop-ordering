package com.fonseca.algashop.ordering.core.application.customer.management;

import com.fonseca.algashop.ordering.core.ports.commons.AddressData;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerUpdateInput;

public class CustomerUpdateInputTestDataBuilder {

    public static CustomerUpdateInput.CustomerUpdateInputBuilder aCustomerUpdate(){
        return CustomerUpdateInput.builder()
                .firstName("Matt")
                .lastName("Damon")
                .phone("123-321-1112")
                .promotionNotificationsAllowed(true)
                .address(AddressData.builder()
                        .street("Amphitheatre Parkway")
                        .number("1600")
                        .complement("Mountain View")
                        .neighborhood("Mountain View")
                        .city("Mountain View")
                        .state("California")
                        .zipCode("94043")
                        .build());
    }
}
