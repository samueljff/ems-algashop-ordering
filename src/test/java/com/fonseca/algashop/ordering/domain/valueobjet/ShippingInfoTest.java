package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShippingInfoTest {

    @Test
    void givenValidData_whenCreateShippingInfoWithBuilder_thenShouldSucceed() {
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
        ShippingInfo shippingInfo = ShippingInfo.builder()
                .fullName(fullName)
                .document(document)
                .phone(phone)
                .address(address)
                .build();

        // then
        Assertions.assertThat(shippingInfo.fullName()).isEqualTo(fullName);
        Assertions.assertThat(shippingInfo.document()).isEqualTo(document);
        Assertions.assertThat(shippingInfo.phone()).isEqualTo(phone);
        Assertions.assertThat(shippingInfo.address()).isEqualTo(address);
    }

    @Test
    void givenValidData_whenCreateShippingInfoWithConstructor_thenShouldSucceed() {
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
        ShippingInfo shippingInfo = new ShippingInfo(fullName, document, phone, address);

        // then
        Assertions.assertThat(shippingInfo.fullName()).isEqualTo(fullName);
        Assertions.assertThat(shippingInfo.document()).isEqualTo(document);
        Assertions.assertThat(shippingInfo.phone()).isEqualTo(phone);
        Assertions.assertThat(shippingInfo.address()).isEqualTo(address);
    }

    @Test
    void givenNullFullName_whenCreateShippingInfo_thenShouldThrowNullPointerException() {
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
                .isThrownBy(() -> new ShippingInfo(new FullName(null, null), document, phone, address));
    }

    @Test
    void givenNullDocument_whenCreateShippingInfo_thenShouldThrowNullPointerException() {
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
                .isThrownBy(() -> new ShippingInfo(fullName, new Document(null), phone, address));
    }

    @Test
    void givenNullPhone_whenCreateShippingInfo_thenShouldThrowNullPointerException() {
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
                .isThrownBy(() -> new ShippingInfo(fullName, document, new Phone(null), address));
    }

    @Test
    void givenNullAddress_whenCreateShippingInfo_thenShouldThrowNullPointerException() {
        // given
        Document document = new Document("12345678900");
        Phone phone = new Phone("11999999999");
        FullName fullName = new FullName("Paulo", "Andrade");

        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ShippingInfo(fullName, document, phone, null));
    }

    @Test
    void givenTwoShippingInfosWithSameValues_whenEquals_thenShouldBeEqual() {
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

        ShippingInfo shippingInfo1 = new ShippingInfo(fullName, document, phone, address);
        ShippingInfo shippingInfo2 = new ShippingInfo(fullName, document, phone, address);

        // then
        Assertions.assertThat(shippingInfo1).isEqualTo(shippingInfo2);
        Assertions.assertThat(shippingInfo1.hashCode()).isEqualTo(shippingInfo2.hashCode());
    }

    @Test
    void givenTwoShippingInfosWithDifferentValues_whenEquals_thenShouldNotBeEqual() {
        // given
        Document document = new Document("12345678900");
        Phone phone = new Phone("11999999999");
        FullName fullName1 = new FullName("Paulo", "Andrade");
        FullName fullName2 = new FullName("Maria", "Santos");
        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("Anonymized")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement(null)
                .build();

        ShippingInfo shippingInfo1 = new ShippingInfo(fullName1, document, phone, address);
        ShippingInfo shippingInfo2 = new ShippingInfo(fullName2, document, phone, address);

        // then
        Assertions.assertThat(shippingInfo1).isNotEqualTo(shippingInfo2);
    }
}