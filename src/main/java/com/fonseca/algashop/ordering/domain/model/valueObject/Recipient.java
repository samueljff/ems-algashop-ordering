package com.fonseca.algashop.ordering.domain.model.valueObject;

import lombok.Builder;

import java.util.Objects;

@Builder
public record Recipient(FullName fullName, Document document, Phone phone) {

    public Recipient {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
    }
}
