package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class BirthDateTest {

    @Test
    void givenPastDate_whenCreateBirthDate_thenShouldSucceed () {
        LocalDate pastDate = LocalDate.of(2000, 1, 1);
        BirthDate birthDate = new BirthDate(pastDate);

        int expectedAge = (int) ChronoUnit.DAYS.between(pastDate, LocalDate.now());
        Assertions.assertThat(birthDate.age()).isEqualTo(expectedAge);
        Assertions.assertThat(birthDate.toString()).isEqualTo(birthDate.toString());
    }

    @Test
    void givenFutureDate_whenCreateBirthDate_thenShouldThrowException() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BirthDate(futureDate));
    }

    @Test
    void givenNullDate_whenCreateBirthDate_thenShouldThrowNullPointerException () {
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(()-> new BirthDate(null));
    }
}