package com.fonseca.algashop.ordering.domain.model.valueObject.id;

import com.fonseca.algashop.ordering.domain.model.utility.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        Objects.requireNonNull(value);
    }

    public ProductId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
