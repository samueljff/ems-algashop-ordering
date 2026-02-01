package com.fonseca.algashop.ordering.domain.model;

public interface Specification<T> {
    boolean isSatisfiedBy(T t);
}
