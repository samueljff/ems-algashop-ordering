package com.fonseca.algashop.ordering.core.domain.model.order;

import com.fonseca.algashop.ordering.core.domain.model.commons.*;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.commons.AddressEmbeddable;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.order.BillingEmbeddable;

public class BillingTestDataBuilder {

    public BillingTestDataBuilder() {
    }

    public static BillingTestDataBuilder anBilling() {

        return new BillingTestDataBuilder();
    }

    public static Billing aBilling() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document("225-09-1992"))
                .phone(new Phone("123-111-9911"))
                .fullName(new FullName("John", "Doe"))
                .email(new Email("jhon.doe@gmail.com"))
                .build();
    }

    public static BillingEmbeddable aBillingEmbeddable() {
        return BillingEmbeddable.builder()
                .address(anAddressEmbeddable())
                .document(String.valueOf(new Document("225-09-1992")))
                .phone(String.valueOf(new Phone("123-111-9911")))
                .firstName("Doe")
                .lastName("Doe")
                .email("jhon.doe@gmail.com")
                .build();
    }

    public static AddressEmbeddable anAddressEmbeddable() {
        return AddressEmbeddable.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .complement("apt. 11")
                .city("Montfort")
                .state("South Carolina")
                .zipCode(String.valueOf(new ZipCode("79911"))).build();
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

    public static Billing aBillingAlt() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document("225-09-002"))
                .phone(new Phone("123-222-9912"))
                .fullName(new FullName("Paulo", "Silva"))
                .email(new Email("paulo.silva@gmail.com"))
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

    public static Billing aBillingNullFullName() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document("225-09-1992"))
                .phone(new Phone("123-111-9911"))
                .fullName(new FullName(null, null))
                .email(new Email("jhon.doe@gmail.com"))
                .build();
    }

    public static Billing aBillingNullDocument() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document(null))
                .phone(new Phone("123-111-9911"))
                .fullName(new FullName("John", "Doe"))
                .email(new Email("jhon.doe@gmail.com"))
                .build();
    }

    public static Billing aBillingNullPhone() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document("225-09-1992"))
                .phone(new Phone(null))
                .fullName(new FullName("John", "Doe"))
                .email(new Email("jhon.doe@gmail.com"))
                .build();
    }

    public static Billing aBillingNullAddress() {
        return Billing.builder()
                .address(null)
                .document(new Document("225-09-1992"))
                .phone(new Phone("123-111-9911"))
                .fullName(new FullName("John", "Doe"))
                .email(new Email("jhon.doe@gmail.com"))
                .build();
    }
}
