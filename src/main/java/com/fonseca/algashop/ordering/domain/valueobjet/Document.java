package com.fonseca.algashop.ordering.domain.valueobjet;

import java.util.Objects;

public record Document(String value) {

    public Document(String value) {
        Objects.requireNonNull(value);
        if (this.value().isBlank()){
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
