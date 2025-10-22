package com.fonseca.algashop.ordering.domain.valueobjet;

import java.util.Objects;

import static com.fonseca.algashop.ordering.domain.exceptions.ErrorMessages.VALIDATION_ERROR_FULLNAME_IS_BLANK;

public record FullName(String firstName, String lastName) {

    public FullName(String firstName, String lastName) {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);

        if(firstName.isBlank()){
            throw new IllegalArgumentException(VALIDATION_ERROR_FULLNAME_IS_BLANK);
        }

        if(lastName.isBlank()){
            throw new IllegalArgumentException(VALIDATION_ERROR_FULLNAME_IS_BLANK);
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();

    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
