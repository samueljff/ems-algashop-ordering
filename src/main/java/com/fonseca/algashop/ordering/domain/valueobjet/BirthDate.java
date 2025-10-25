package com.fonseca.algashop.ordering.domain.valueobjet;

import com.fonseca.algashop.ordering.domain.exceptions.ErrorMessages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record BirthDate(LocalDate value) {

    public BirthDate {
        Objects.requireNonNull(value);
        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
        }
    }

    public Integer age() {
        return (int) ChronoUnit.DAYS.between(value, LocalDate.now());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
