package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {
    @Test
    void givenValidPhone_whenCreatePhone_thenShouldSucceed() {
        // given
        String validPhone = "11-99999-8888";

        // when
        Phone phone = new Phone(validPhone);

        // then
        Assertions.assertThat(phone.value()).isEqualTo(validPhone);
    }

    @Test
    void givenValidPhone_whenToString_thenShouldReturnValue() {
        // given
        Phone phone = new Phone("21-88888-7777");

        // when
        String result = phone.toString();

        // then
        Assertions.assertThat(result).isEqualTo("21-88888-7777");
    }

    @Test
    void givenNullPhone_whenCreatePhone_thenShouldThrowNullPointerException() {
        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Phone(null));
    }

    @Test
    void givenEmptyPhone_whenCreatePhone_thenShouldThrowIllegalArgumentException() {
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Phone(""));
    }

    @Test
    void givenBlankPhone_whenCreatePhone_thenShouldThrowIllegalArgumentException() {
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Phone("   "));
    }
}