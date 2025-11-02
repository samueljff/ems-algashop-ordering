package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    @Test
    void givenValidValue_whenCreateQuantity_thenShouldSucceed() {
        // given
        Integer value = 5;

        // when
        Quantity quantity = new Quantity(value);

        // then
        Assertions.assertThat(quantity.value()).isEqualTo(5);
    }

    @Test
    void givenZeroConstant_whenAccess_thenShouldReturnZero() {
        // then
        Assertions.assertThat(Quantity.ZERO.value()).isEqualTo(0);
    }

    @Test
    void givenZeroValue_whenCreateQuantity_thenShouldSucceed() {
        // given
        Integer value = 0;

        // when
        Quantity quantity = new Quantity(value);

        // then
        Assertions.assertThat(quantity.value()).isEqualTo(0);
    }

    @Test
    void givenNullValue_whenCreateQuantity_thenShouldThrowNullPointerException() {
        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Quantity(null));
    }

    @Test
    void givenNegativeValue_whenCreateQuantity_thenShouldThrowIllegalArgumentException() {
        // given
        Integer negativeValue = -1;

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Quantity(negativeValue));
    }

    @Test
    void givenTwoQuantities_whenAdd_thenShouldReturnSum() {
        // given
        Quantity quantity1 = new Quantity(3);
        Quantity quantity2 = new Quantity(5);

        // when
        Quantity result = quantity1.add(quantity2);

        // then
        Assertions.assertThat(result.value()).isEqualTo(8);
    }

    @Test
    void givenQuantityAndZero_whenAdd_thenShouldReturnSameQuantity() {
        // given
        Quantity quantity = new Quantity(5);

        // when
        Quantity result = quantity.add(Quantity.ZERO);

        // then
        Assertions.assertThat(result.value()).isEqualTo(5);
    }

    @Test
    void givenNullQuantity_whenAdd_thenShouldThrowNullPointerException() {
        // given
        Quantity quantity = new Quantity(5);

        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> quantity.add(null));
    }

    @Test
    void givenValidQuantity_whenToString_thenShouldReturnValueAsString() {
        // given
        Quantity quantity = new Quantity(42);

        // when
        String result = quantity.toString();

        // then
        Assertions.assertThat(result).isEqualTo("42");
    }

    @Test
    void givenSmallerQuantity_whenCompareTo_thenShouldReturnNegative() {
        // given
        Quantity quantity1 = new Quantity(3);
        Quantity quantity2 = new Quantity(5);

        // when
        int comparison = quantity1.compareTo(quantity2);

        // then
        Assertions.assertThat(comparison).isNegative();
    }

    @Test
    void givenGreaterQuantity_whenCompareTo_thenShouldReturnPositive() {
        // given
        Quantity quantity1 = new Quantity(5);
        Quantity quantity2 = new Quantity(3);

        // when
        int comparison = quantity1.compareTo(quantity2);

        // then
        Assertions.assertThat(comparison).isPositive();
    }

    @Test
    void givenEqualQuantity_whenCompareTo_thenShouldReturnZero() {
        // given
        Quantity quantity1 = new Quantity(5);
        Quantity quantity2 = new Quantity(5);

        // when
        int comparison = quantity1.compareTo(quantity2);

        // then
        Assertions.assertThat(comparison).isZero();
    }

    @Test
    void givenTwoQuantitiesWithSameValue_whenEquals_thenShouldBeEqual() {
        // given
        Quantity quantity1 = new Quantity(10);
        Quantity quantity2 = new Quantity(10);

        // then
        Assertions.assertThat(quantity1).isEqualTo(quantity2);
        Assertions.assertThat(quantity1.hashCode()).isEqualTo(quantity2.hashCode());
    }

    @Test
    void givenTwoQuantitiesWithDifferentValues_whenEquals_thenShouldNotBeEqual() {
        // given
        Quantity quantity1 = new Quantity(10);
        Quantity quantity2 = new Quantity(20);

        // then
        Assertions.assertThat(quantity1).isNotEqualTo(quantity2);
    }

    @Test
    void givenMaxIntegerValue_whenCreateQuantity_thenShouldSucceed() {
        // given
        Integer maxValue = Integer.MAX_VALUE;

        // when
        Quantity quantity = new Quantity(maxValue);

        // then
        Assertions.assertThat(quantity.value()).isEqualTo(Integer.MAX_VALUE);
    }
}