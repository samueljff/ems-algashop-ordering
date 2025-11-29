package com.fonseca.algashop.ordering.domain.model.valueObject;

import java.time.LocalDate;

public class ShippingTestDataBuilder {

    public ShippingTestDataBuilder() {
    }

    public static ShippingTestDataBuilder anShipping() {

        return new ShippingTestDataBuilder();
    }

    public static Shipping aShipping() {
        return Shipping.builder()
                .cost(new Money("10"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .address(anAddress())
                .recipient(Recipient.builder()
                        .fullName(new FullName("John", "Doe"))
                        .document(new Document("112-33-2321"))
                        .phone(new Phone("111-441-1244"))
                        .build())
                .build();
    }

    public static Address anAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .complement("apt. 11")
                .city("Montfort")
                .state("South Carolina")
                .zipCode(new ZipCode("79911")).build();
    }

    public static Shipping aShippingAlt() {
        return Shipping.builder()
                .cost(new Money("20.00"))
                .expectedDate(LocalDate.now().plusWeeks(2))
                .address(anAddressAlt())
                .recipient(Recipient.builder()
                        .fullName(new FullName("Mary", "Jones"))
                        .document(new Document("552-11-4333"))
                        .phone(new Phone("54-454-1144"))
                        .build())
                .build();
    }

    public static Address anAddressAlt() {
        return Address.builder()
                .street("Sansome Street")
                .number("875")
                .neighborhood("Sansome")
                .city("San Francisco")
                .state("California")
                .zipCode(new ZipCode("08040"))
                .build();
    }

    public static Shipping aShippingNullFullName() {
        return Shipping.builder()
                .cost(new Money("10"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .address(anAddress())
                .recipient(Recipient.builder()
                        .fullName(new FullName(null, null))
                        .document(new Document("112-33-2321"))
                        .phone(new Phone("111-441-1244"))
                        .build())
                .build();
    }

    public static Shipping aShippingNullDocument() {
        return Shipping.builder()
                .cost(new Money("10"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .address(anAddress())
                .recipient(Recipient.builder()
                        .fullName(new FullName("Mary", "Jones"))
                        .document(new Document(null))
                        .phone(new Phone("111-441-1244"))
                        .build())
                .build();
    }

    public static Shipping aShippingNullPhone() {
        return Shipping.builder()
                .cost(new Money("10"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .address(anAddress())
                .recipient(Recipient.builder()
                        .fullName(new FullName("John", "Doe"))
                        .document(new Document("112-33-2321"))
                        .phone(new Phone(null))
                        .build())
                .build();
    }

    public static Shipping aShippingNullAddress() {
        return Shipping.builder()
                .cost(new Money("10"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .address(null)
                .recipient(Recipient.builder()
                        .fullName(new FullName("John", "Doe"))
                        .document(new Document("112-33-2321"))
                        .phone(new Phone("111-441-1244"))
                        .build())
                .build();
    }
}
