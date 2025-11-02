package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void givenValidBigDecimal_whenCreateMoney_thenShouldSucceed() {
        // given
        BigDecimal value = new BigDecimal("10.50");

        // when
        Money money = new Money(value);

        // then
        Assertions.assertThat(money.value()).isEqualByComparingTo(new BigDecimal("10.50"));
    }

    @Test
    void givenValidString_whenCreateMoney_thenShouldSucceed() {
        // given
        String value = "10.50";

        // when
        Money money = new Money(value);

        // then
        Assertions.assertThat(money.value()).isEqualByComparingTo(new BigDecimal("10.50"));
    }

    @Test
    void givenValueWithMoreThanTwoDecimals_whenCreateMoney_thenShouldRoundToTwoDecimals() {
        // given
        BigDecimal value = new BigDecimal("10.555");

        // when
        Money money = new Money(value);

        // then
        Assertions.assertThat(money.value()).isEqualByComparingTo(new BigDecimal("10.56"));
    }

    @Test
    void givenZeroConstant_whenAccess_thenShouldReturnZero() {
        // then
        Assertions.assertThat(Money.ZERO.value()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void givenNullValue_whenCreateMoney_thenShouldThrowNullPointerException() {
        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Money((BigDecimal) null));
    }

    @Test
    void givenNegativeValue_whenCreateMoney_thenShouldThrowIllegalArgumentException() {
        // given
        String negativeValue = "-10.50";

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money(negativeValue))
                .withMessageContaining("valor monetário não pode ser negativo");
    }

    @Test
    void givenValidMoneyAndQuantity_whenMultiply_thenShouldReturnCorrectResult() {
        // given
        Money money = new Money("10.50");
        Quantity quantity = new Quantity(3);

        // when
        Money result = money.multiply(quantity);

        // then
        Assertions.assertThat(result.value()).isEqualByComparingTo(new BigDecimal("31.50"));
    }

    @Test
    void givenNullQuantity_whenMultiply_thenShouldThrowNullPointerException() {
        // given
        Money money = new Money("10.50");

        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> money.multiply(null));
    }

    @Test
    void givenQuantityLessThanOne_whenMultiply_thenShouldThrowIllegalArgumentException() {
        // given
        Money money = new Money("10.50");
        Quantity quantity = new Quantity(0);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> money.multiply(quantity));
    }

    @Test
    void givenTwoMoneyValues_whenAdd_thenShouldReturnSum() {
        // given
        Money money1 = new Money("10.50");
        Money money2 = new Money("5.25");

        // when
        Money result = money1.add(money2);

        // then
        Assertions.assertThat(result.value()).isEqualByComparingTo(new BigDecimal("15.75"));
    }

    @Test
    void givenNullMoney_whenAdd_thenShouldThrowNullPointerException() {
        // given
        Money money = new Money("10.50");

        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> money.add(null));
    }

    @Test
    void givenTwoMoneyValues_whenDivide_thenShouldReturnQuotient() {
        // given
        Money dividend = new Money("10.00");
        Money divisor = new Money("4.00");

        // when
        Money result = dividend.divide(divisor);

        // then
        Assertions.assertThat(result.value()).isEqualByComparingTo(new BigDecimal("2.50"));
    }

    @Test
    void givenDivisionWithRemainder_whenDivide_thenShouldRoundToTwoDecimals() {
        // given
        Money dividend = new Money("10.00");
        Money divisor = new Money("3.00");

        // when
        Money result = dividend.divide(divisor);

        // then
        Assertions.assertThat(result.value()).isEqualByComparingTo(new BigDecimal("3.33"));
    }

    @Test
    void givenValidMoney_whenToString_thenShouldReturnValueAsString() {
        // given
        Money money = new Money("10.50");

        // when
        String result = money.toString();

        // then
        Assertions.assertThat(result).isEqualTo("10.50");
    }

    @Test
    void givenSmallerMoney_whenCompareTo_thenShouldReturnNegative() {
        // given
        Money money1 = new Money("5.00");
        Money money2 = new Money("10.00");

        // when
        int comparison = money1.compareTo(money2);

        // then
        Assertions.assertThat(comparison).isNegative();
    }

    @Test
    void givenGreaterMoney_whenCompareTo_thenShouldReturnPositive() {
        // given
        Money money1 = new Money("10.00");
        Money money2 = new Money("5.00");

        // when
        int comparison = money1.compareTo(money2);

        // then
        Assertions.assertThat(comparison).isPositive();
    }

    @Test
    void givenEqualMoney_whenCompareTo_thenShouldReturnZero() {
        // given
        Money money1 = new Money("10.00");
        Money money2 = new Money("10.00");

        // when
        int comparison = money1.compareTo(money2);

        // then
        Assertions.assertThat(comparison).isZero();
    }

    @Test
    void givenTwoMoneyWithSameValue_whenEquals_thenShouldBeEqual() {
        // given
        Money money1 = new Money("10.50");
        Money money2 = new Money("10.50");

        // then
        Assertions.assertThat(money1).isEqualTo(money2);
        Assertions.assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
    }

    @Test
    void givenTwoMoneyWithDifferentValues_whenEquals_thenShouldNotBeEqual() {
        // given
        Money money1 = new Money("10.50");
        Money money2 = new Money("20.00");

        // then
        Assertions.assertThat(money1).isNotEqualTo(money2);
    }
}