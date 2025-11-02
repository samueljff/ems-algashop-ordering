package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BillingInfoTest {
    @Test
    void givenValidData_whenCreateBillingInfoWithBuilder_thenShouldSucceed() {
        // given
        Document document = new Document("12345678900");
        Phone phone = new Phone("11999999999");
        FullName fullName = new FullName("Paulo", "Andrade");
        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("Anonymized")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement(null)
                .build();

        // when
        BillingInfo billingInfo = BillingInfo.builder()
                .fullName(fullName)
                .document(document)
                .phone(phone)
                .address(address)
                .build();

        // then
        Assertions.assertThat(billingInfo.fullName()).isEqualTo(fullName);
        Assertions.assertThat(billingInfo.document()).isEqualTo(document);
        Assertions.assertThat(billingInfo.phone()).isEqualTo(phone);
        Assertions.assertThat(billingInfo.address()).isEqualTo(address);
    }

    @Test
    void givenValidData_whenCreateBillingInfoWithConstructor_thenShouldSucceed() {
        // given
        Document document = new Document("12345678900");
        Phone phone = new Phone("11999999999");
        FullName fullName = new FullName("Paulo", "Andrade");
        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("Anonymized")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement(null)
                .build();
        // when
        BillingInfo billingInfo = new BillingInfo(fullName, document, phone, address);

        // then
        Assertions.assertThat(billingInfo.fullName()).isEqualTo(fullName);
        Assertions.assertThat(billingInfo.document()).isEqualTo(document);
        Assertions.assertThat(billingInfo.phone()).isEqualTo(phone);
        Assertions.assertThat(billingInfo.address()).isEqualTo(address);
    }

    @Test
    void givenNullFullName_whenCreateBillingInfo_thenShouldThrowNullPointerException() {
        // given
        Document document = new Document("12345678900");
        Phone phone = new Phone("11999999999");
        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("Anonymized")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement(null)
                .build();

        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BillingInfo(new FullName(null, null), document, phone, address));
    }

    @Test
    void givenNullDocument_whenCreateBillingInfo_thenShouldThrowNullPointerException() {
        // given
        Phone phone = new Phone("11999999999");
        FullName fullName = new FullName("Paulo", "Andrade");
        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("Anonymized")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement(null)
                .build();

        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BillingInfo(fullName, new Document(null), phone, address));
    }

    @Test
    void givenNullPhone_whenCreateBillingInfo_thenShouldThrowNullPointerException() {
        // given
        // given
        Document document = new Document("12345678900");
        FullName fullName = new FullName("Paulo", "Andrade");
        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("Anonymized")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement(null)
                .build();

        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BillingInfo(fullName, document, new Phone(null), address));
    }

    @Test
    void givenNullAddress_whenCreateBillingInfo_thenShouldThrowNullPointerException() {
        // given
        // given
        Document document = new Document("12345678900");
        Phone phone = new Phone("11999999999");
        FullName fullName = new FullName("Paulo", "Andrade");

        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BillingInfo(fullName, document, phone, null));
    }

    @Test
    void givenTwoBillingInfosWithSameValues_whenEquals_thenShouldBeEqual() {
        // given
        // given
        Document document = new Document("12345678900");
        Phone phone = new Phone("11999999999");
        FullName fullName = new FullName("Paulo", "Andrade");
        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("Anonymized")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement(null)
                .build();

        BillingInfo billingInfo1 = new BillingInfo(fullName, document, phone, address);
        BillingInfo billingInfo2 = new BillingInfo(fullName, document, phone, address);

        // then
        Assertions.assertThat(billingInfo1).isEqualTo(billingInfo2);
        Assertions.assertThat(billingInfo1.hashCode()).isEqualTo(billingInfo2.hashCode());
    }

    @Test
    void givenTwoBillingInfosWithDifferentValues_whenEquals_thenShouldNotBeEqual() {
        // given
        Document document = new Document("12345678900");
        Phone phone = new Phone("11999999999");
        FullName fullName1 = new FullName("Paulo", "Andrade");
        FullName fullName2 = new FullName("Maria", "Sousa");
        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("Anonymized")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement(null)
                .build();

        BillingInfo billingInfo1 = new BillingInfo(fullName1, document, phone, address);
        BillingInfo billingInfo2 = new BillingInfo(fullName2, document, phone, address);

        // then
        Assertions.assertThat(billingInfo1).isNotEqualTo(billingInfo2);
    }

    @Test
    void givenBillingInfoAndShippingInfo_whenCompare_thenShouldBeDistinctTypes() {
        // given
        Document document = new Document("12345678900");
        Phone phone = new Phone("11999999999");
        FullName fullName = new FullName("Paulo", "Andrade");
        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("Anonymized")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement(null)
                .build();

        BillingInfo billingInfo = new BillingInfo(fullName, document, phone, address);
        ShippingInfo shippingInfo = new ShippingInfo(fullName, document, phone, address);

        // then
        Assertions.assertThat(billingInfo).isNotEqualTo(shippingInfo);
        Assertions.assertThat(billingInfo.getClass()).isNotEqualTo(shippingInfo.getClass());
    }
}