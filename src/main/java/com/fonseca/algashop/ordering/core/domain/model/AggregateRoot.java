package com.fonseca.algashop.ordering.core.domain.model;

public interface AggregateRoot <ID> extends DomainEventSource {
    ID id();
}
