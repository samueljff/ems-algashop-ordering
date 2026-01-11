package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.order.shipping.ShippingTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BillingTest {
    @Test
    void givenValidData_whenCreateBillingWithBuilder_thenShouldSucceed() {
        // given/when
        Billing billing = BillingTestDataBuilder.aBilling();
        Billing order = OrderTestDataBuilder.aBilling();

        // then
        Assertions.assertThat(billing.fullName()).isEqualTo(order.fullName());
        Assertions.assertThat(billing.document()).isEqualTo(order.document());
        Assertions.assertThat(billing.phone()).isEqualTo(order.phone());
        Assertions.assertThat(billing.address()).isEqualTo(order.address());
    }

    @Test
    void givenNullFullName_whenCreateBilling_thenShouldThrowNullPointerException() {
        //given/when/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> BillingTestDataBuilder.aBillingNullFullName());
    }

    @Test
    void givenNullDocument_whenCreateBilling_thenShouldThrowNullPointerException() {
        // given/when/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> BillingTestDataBuilder.aBillingNullDocument());
    }

    @Test
    void givenNullPhone_whenCreateBilling_thenShouldThrowNullPointerException() {
        // given/when/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> BillingTestDataBuilder.aBillingNullPhone());
    }

    @Test
    void givenNullAddress_whenCreateBilling_thenShouldThrowNullPointerException() {
        // given/when/then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> BillingTestDataBuilder.aBillingNullAddress());
    }

    @Test
    void givenTwoBillingWithSameValues_whenEquals_thenShouldBeEqual() {
        // given/when

        Billing billing1 = BillingTestDataBuilder.aBilling();
        Billing billing2 = BillingTestDataBuilder.aBilling();

        // then
        Assertions.assertThat(billing1).isEqualTo(billing2);
        Assertions.assertThat(billing1.hashCode()).isEqualTo(billing2.hashCode());
    }

    @Test
    void givenTwoBillingWithDifferentValues_whenEquals_thenShouldNotBeEqual() {
        // given/when
        Billing billing1 = BillingTestDataBuilder.aBilling();
        Billing billing2 = BillingTestDataBuilder.aBillingAlt();

        // then
        Assertions.assertThat(billing1).isNotEqualTo(billing2);
    }

    @Test
    void givenBillingAndShipping_whenCompare_thenShouldBeDistinctTypes() {
        // given/when
        Shipping shipping = ShippingTestDataBuilder.aShipping();
        Billing billing = BillingTestDataBuilder.aBilling();

        // then
        Assertions.assertThat(billing).isNotEqualTo(shipping);
        Assertions.assertThat(billing.getClass()).isNotEqualTo(shipping.getClass());
    }
}