package com.fonseca.algashop.ordering.infrastructure.fake;

import com.fonseca.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.fonseca.algashop.ordering.domain.model.commons.Address;
import com.fonseca.algashop.ordering.domain.model.commons.ZipCode;
import org.springframework.stereotype.Component;

@Component
public class FixedOriginAddressService implements OriginAddressService {

    @Override
    public Address originAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1134")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .build();
    }
}
