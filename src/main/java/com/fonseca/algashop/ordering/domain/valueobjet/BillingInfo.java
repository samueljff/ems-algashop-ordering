package com.fonseca.algashop.ordering.domain.valueobjet;

import lombok.Builder;

import java.util.Objects;

@Builder
public record BillingInfo(FullName fullName, Document document, Phone phone, Address address) {
    public BillingInfo {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(address);
    }
}
