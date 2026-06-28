package com.fonseca.algashop.ordering.core.application.checkout;

import com.fonseca.algashop.ordering.core.application.commons.AddressData;
import com.fonseca.algashop.ordering.core.domain.model.commons.*;
import com.fonseca.algashop.ordering.core.domain.model.order.Recipient;
import com.fonseca.algashop.ordering.core.domain.model.order.Shipping;
import com.fonseca.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import org.springframework.stereotype.Component;

@Component
public class ShippingInputDisassembler {

    public Shipping toDomainModel(ShippingInput shippingInput, ShippingCostService.CalculationResult shippingCalculationResult) {
        AddressData address = shippingInput.getAddress();
        return Shipping.builder()
                .cost(shippingCalculationResult.cost())
                .expectedDate(shippingCalculationResult.expectedDate())
                .recipient(Recipient.builder()
                        .fullName(new FullName(shippingInput.getRecipient().getFirstName(), shippingInput.getRecipient().getLastName()))
                        .document(new Document(shippingInput.getRecipient().getDocument()))
                        .phone(new Phone(shippingInput.getRecipient().getPhone()))
                        .build())
                .address(Address.builder()
                        .street(address.getStreet())
                        .number(address.getNumber())
                        .complement(address.getComplement())
                        .neighborhood(address.getNeighborhood())
                        .city(address.getCity())
                        .state(address.getState())
                        .zipCode(new ZipCode(address.getZipCode()))
                        .build())
                .build();
    }
}
