package com.fonseca.algashop.ordering.domain.model;

public interface RemoveCapableRepository<T extends AggregateRoot<ID>, ID> extends Repository<T, ID> {

    void remove(T t);
    void remove(ID id);
}
