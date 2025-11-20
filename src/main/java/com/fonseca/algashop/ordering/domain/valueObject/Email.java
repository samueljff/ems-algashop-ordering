package com.fonseca.algashop.ordering.domain.valueObject;

import com.fonseca.algashop.ordering.domain.validator.FieldValidations;

public record Email(String value) {

    public Email {
        FieldValidations.requiresValidEmail(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
