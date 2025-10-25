package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void givenValidEmail_whenCreateEmail_thenShouldSucceed() {
        // given
        String validEmail = "john.doe@example.com";

        // when
        Email email = new Email(validEmail);

        // then
        Assertions.assertThat(email.value()).isEqualTo(validEmail);
        Assertions.assertThat(email.toString()).isEqualTo(validEmail);
    }

    @Test
    void givenNullEmail_whenCreateEmail_thenShouldThrowNullPointerException() {
        Assertions.assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenBlankEmail_whenCreateEmail_thenShouldThrowIllegalArgumentException() {
        // given
        String blankEmail = " ";
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()-> new Email(blankEmail));
    }

    @Test
    void givenInvalidEmail_whenCreateEmail_thenShouldThrowIllegalArgumentException() {
        // given
        String invalidEmail = "john.doe@@example";

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()-> new Email(invalidEmail));
    }
}