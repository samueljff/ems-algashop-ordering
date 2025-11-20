package com.fonseca.algashop.ordering.domain.valueobjet;

import com.fonseca.algashop.ordering.domain.entity.OrderTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.fonseca.algashop.ordering.domain.entity.OrderTestDataBuilder.anAddressAlt;

class ShippingTest {

    @Test
    void givenValidData_whenCreateShippingInfoWithBuilder_thenShouldSucceed() {
        // given
        Document document = new Document("112-33-2321");
        Phone phone = new Phone("111-441-1244");
        FullName fullName = new FullName("John", "Doe");

        Address address =  Address.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .city("Montfort")
                .state("South Carolina")
                .zipCode(new ZipCode("79911"))
                .complement("apt. 11")
                .build();
        // when
        Shipping shipping = OrderTestDataBuilder.aShipping();

        // then
        Assertions.assertThat(shipping.recipient().fullName()).isEqualTo(fullName);
        Assertions.assertThat(shipping.recipient().document()).isEqualTo(document);
        Assertions.assertThat(shipping.recipient().phone()).isEqualTo(phone);
        Assertions.assertThat(shipping.address()).isEqualTo(address);
    }

    @Test
    void givenNullFullName_whenCreateShippingInfo_thenShouldThrowNullPointerException() {
        // given/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    Shipping shipping = OrderTestDataBuilder.aShipping().toBuilder()
                            .recipient(Recipient.builder()
                                    .fullName(new FullName(null, null))
                                    .build())
                            .build();

                });
    }

    @Test
    void givenNullDocument_whenCreateShippingInfo_thenShouldThrowNullPointerException() {
        // given/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    Shipping shipping = OrderTestDataBuilder.aShipping().toBuilder()
                            .recipient(Recipient.builder()
                                    .fullName(new FullName(null, null))
                                    .build())
                            .build();
                });
    }

    @Test
    void givenNullPhone_whenCreateShippingInfo_thenShouldThrowNullPointerException() {
        // given/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {Shipping shipping = OrderTestDataBuilder.aShipping().toBuilder()
                        .recipient(Recipient.builder()
                                .fullName(new FullName(null, null))
                                .build())
                        .build();
                });
    }

    @Test
    void givenNullAddress_whenCreateShippingInfo_thenShouldThrowNullPointerException() {
        // when/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    Address address = anAddressAlt().toBuilder()
                            .street(null)
                            .number(null)
                            .neighborhood(null)
                            .city("San Francisco")
                            .state("California")
                            .zipCode(new ZipCode("08040"))
                            .build();
                });
    }

    @Test
    void givenTwoShippingInfosWithSameValues_whenEquals_thenShouldBeEqual() {
        // given
        Shipping shipping = OrderTestDataBuilder.aShipping();

        Shipping shipping1 = shipping;
        Shipping shipping2 = shipping;

        // then
        Assertions.assertThat(shipping1).isEqualTo(shipping2);
        Assertions.assertThat(shipping1.hashCode()).isEqualTo(shipping2.hashCode());
    }

    @Test
    void givenTwoShippingInfosWithDifferentValues_whenEquals_thenShouldNotBeEqual() {
        // given
        Shipping shipping1 = OrderTestDataBuilder.aShipping().toBuilder()
                .cost(new Money("20.00"))
                .expectedDate(LocalDate.now().plusWeeks(2))
                .address(anAddressAlt())
                .recipient(Recipient.builder()
                        .fullName(new FullName("Mary", "Jones"))
                        .document(new Document("552-11-4333"))
                        .phone(new Phone("54-454-1144"))
                        .build())
                .build();

        Shipping shipping2 = OrderTestDataBuilder.aShipping();

        // then
        Assertions.assertThat(shipping1).isNotEqualTo(shipping2);
    }
}