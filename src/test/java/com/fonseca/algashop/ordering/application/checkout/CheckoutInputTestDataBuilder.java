package com.fonseca.algashop.ordering.application.checkout;

import com.fonseca.algashop.ordering.application.commons.AddressData;

import java.util.UUID;

public class CheckoutInputTestDataBuilder {

    public static CheckoutInput.CheckoutInputBuilder aCheckoutInput() {
        return CheckoutInput.builder()
                .shoppingCartId(UUID.randomUUID())
                .paymentMethod("CREDIT_CARD")
                .shipping(ShippingInput.builder()
                        .recipient(RecipientData.builder()
                                .firstName("Alice")
                                .lastName("Smith")
                                .document("987-65-4321")
                                .phone("555-123-4567")
                                .build())
                        .address(AddressData.builder()
                                .street("Oak Avenue")
                                .number("789")
                                .complement("Apartment 3C")
                                .neighborhood("Downtown")
                                .city("Portland")
                                .state("Oregon")
                                .zipCode("97201")
                                .build())
                        .build())
                .billing(BillingData.builder()
                        .firstName("Robert")
                        .lastName("Johnson")
                        .phone("555-987-6543")
                        .document("456-78-9012")
                        .email("robert.johnson@email.com")
                        .address(AddressData.builder()
                                .street("Sunset Boulevard")
                                .number("2500")
                                .complement("Suite 100")
                                .neighborhood("West Hollywood")
                                .city("Los Angeles")
                                .state("California")
                                .zipCode("90046")
                                .build())
                        .build());
    }
}
