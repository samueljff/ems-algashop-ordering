package com.fonseca.algashop.ordering.domain.valueobjet.id;

import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record OrderId(TSID value) {

    public OrderId {
        Objects.requireNonNull(value);
    }

    public OrderId (Long value) {
        this(TSID.from(value));
    }

    public OrderId (String value) {
        this(TSID.from(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
