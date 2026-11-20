package com.fonseca.algashop.ordering.core.ports.in.order;

import com.fonseca.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderSummaryOutput;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ForQueryingOrders {
    OrderDetailOutput findById(String id);
    OrderDetailOutput findByIdAndCustomerId(String id, UUID customerId);
    Page<OrderSummaryOutput> filter(OrderFilter filter);
}
