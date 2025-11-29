package com.fonseca.algashop.ordering.domain.model.valueObject;

import com.fonseca.algashop.ordering.domain.model.validator.FieldValidations;

public record ProductName(String value) {

    public ProductName {
        FieldValidations.requiresNonBlank(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
