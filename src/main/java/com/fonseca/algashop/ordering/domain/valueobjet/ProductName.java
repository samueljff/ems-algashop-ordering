package com.fonseca.algashop.ordering.domain.valueobjet;

import com.fonseca.algashop.ordering.domain.validator.FieldValidations;

public record ProductName(String value) {

    public ProductName {
        FieldValidations.requiresNonBlank(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
