package com.fonseca.algashop.ordering.domain.model;

public interface AggregateRoot <ID> extends DomainEventSource {
    ID id();
}
