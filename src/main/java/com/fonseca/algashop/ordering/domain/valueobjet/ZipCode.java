package com.fonseca.algashop.ordering.domain.valueobjet;

import java.util.Objects;

public record ZipCode(String value) {

    public ZipCode {
        Objects.requireNonNull(value);

        if (value.isBlank()){
            throw new IllegalArgumentException();
        }

        if(value.length() != 5){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
