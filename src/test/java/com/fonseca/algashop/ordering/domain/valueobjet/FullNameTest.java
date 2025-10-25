package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.fonseca.algashop.ordering.domain.exceptions.ErrorMessages.VALIDATION_ERROR_FULLNAME_IS_BLANK;
import static org.junit.jupiter.api.Assertions.*;

class FullNameTest {

    @Test
    void givenValidNames_whenCreateFullName_thenShouldSucceed() {
        // given
        String first = "John";
        String last = "Doe";

        // when
        FullName fullName = new FullName(first, last);

        // then
        Assertions.assertThat(fullName.firstName()).isEqualTo("John");
        Assertions.assertThat(fullName.lastName()).isEqualTo("Doe");
        Assertions.assertThat(fullName.toString()).isEqualTo("John Doe");
    }

    @Test
    void givenNamesWithExtraSpaces_whenCreateFullName_thenShouldTrimAndSucceed() {
        // given
        String first = "  Jane  ";
        String last = "  Smith ";

        // when
        FullName fullName = new FullName(first, last);

        // then
        Assertions.assertThat(fullName.firstName()).isEqualTo("Jane");
        Assertions.assertThat(fullName.lastName()).isEqualTo("Smith");
        Assertions.assertThat(fullName.toString()).isEqualTo("Jane Smith");
    }

    @Test
    void givenNullFirstName_whenCreateFullName_thenShouldThrowNullPointerException() {
        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName(null, "Doe"));
    }

    @Test
    void givenNullLastName_whenCreateFullName_thenShouldThrowNullPointerException() {
        // then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName("John", null));
    }

    @Test
    void givenBlankFirstName_whenCreateFullName_thenShouldThrowIllegalArgumentException() {
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("   ", "Doe"))
                .withMessage(VALIDATION_ERROR_FULLNAME_IS_BLANK);
    }

    @Test
    void givenBlankLastName_whenCreateFullName_thenShouldThrowIllegalArgumentException() {
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("John", "   "))
                .withMessage(VALIDATION_ERROR_FULLNAME_IS_BLANK);
    }

    @Test
    void givenValidFullName_whenToString_thenShouldReturnConcatenatedNames() {
        // given
        FullName fullName = new FullName("Alice", "Johnson");

        // when
        String result = fullName.toString();

        // then
        Assertions.assertThat(result).isEqualTo("Alice Johnson");
    }
}