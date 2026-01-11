package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.order.shipping.ShippingTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShippingTest {

    @Test
    void givenValidData_whenCreateShippingWithBuilder_thenShouldSucceed() {
        // given/when
        Shipping shipping = ShippingTestDataBuilder.aShipping();
        Shipping order = OrderTestDataBuilder.aShipping();

        // then
        Assertions.assertThat(shipping.recipient().fullName()).isEqualTo(order.recipient().fullName());
        Assertions.assertThat(shipping.recipient().document()).isEqualTo(order.recipient().document());
        Assertions.assertThat(shipping.recipient().phone()).isEqualTo(order.recipient().phone());
        Assertions.assertThat(shipping.address()).isEqualTo(order.address());
    }

    @Test
    void givenNullFullName_whenCreateShipping_thenShouldThrowNullPointerException() {
        // given/when/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    Shipping shipping = ShippingTestDataBuilder.aShippingNullFullName();
                });
    }

    @Test
    void givenNullDocument_whenCreateShipping_thenShouldThrowNullPointerException() {
        // given/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    Shipping shipping = ShippingTestDataBuilder.aShippingNullDocument();
                });
    }

    @Test
    void givenNullPhone_whenCreateShipping_thenShouldThrowNullPointerException() {
        // given/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {Shipping shipping = ShippingTestDataBuilder.aShippingNullPhone();
                });
    }

    @Test
    void givenNullAddress_whenCreateShipping_thenShouldThrowNullPointerException() {
        // when/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    Shipping address = ShippingTestDataBuilder.aShippingNullAddress();
                });
    }

    @Test
    void givenTwoShippingWithSameValues_whenEquals_thenShouldBeEqual() {
        // given/when
        Shipping shipping = ShippingTestDataBuilder.aShipping();
        Shipping shipping1 = shipping;
        Shipping shipping2 = shipping;

        // then
        Assertions.assertThat(shipping1).isEqualTo(shipping2);
        Assertions.assertThat(shipping1.hashCode()).isEqualTo(shipping2.hashCode());
    }

    @Test
    void givenTwoShippingWithDifferentValues_whenEquals_thenShouldNotBeEqual() {
        // given/when
        Shipping shipping1 = ShippingTestDataBuilder.aShipping();
        Shipping shipping2 = OrderTestDataBuilder.aShippingAlt();

        // then
        Assertions.assertThat(shipping1).isNotEqualTo(shipping2);
    }
}