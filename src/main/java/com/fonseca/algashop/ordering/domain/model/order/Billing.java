package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.commons.*;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Billing(FullName fullName, Document document, Phone phone, Email email, Address address) {
    public Billing {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(address);
        Objects.requireNonNull(email);
    }
}
