package com.fonseca.algashop.ordering.domain.model;

import java.util.Objects;
import java.util.UUID;

public record CreditCardId(UUID id) {
    public CreditCardId (){
        this(IdGenerator.generateTimeBasedUUID());
    }

    public CreditCardId {
        Objects.requireNonNull(id);
    }
}
