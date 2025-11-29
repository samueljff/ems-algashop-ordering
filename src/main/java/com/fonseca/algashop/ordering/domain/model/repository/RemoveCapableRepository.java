package com.fonseca.algashop.ordering.domain.model.repository;

import com.fonseca.algashop.ordering.domain.model.entity.AggregateRoot;

public interface RemoveCapableRepository<T extends AggregateRoot<ID>, ID> extends Repository<T, ID> {

    void remove(T t);
    void remove(ID id);
}
