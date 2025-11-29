package com.fonseca.algashop.ordering.domain.model.repository;

import com.fonseca.algashop.ordering.domain.model.entity.Order;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.OrderId;

public interface Orders extends Repository<Order, OrderId> {
}
