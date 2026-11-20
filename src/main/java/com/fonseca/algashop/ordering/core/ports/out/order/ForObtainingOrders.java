package com.fonseca.algashop.ordering.core.ports.out.order;

import com.fonseca.algashop.ordering.core.ports.in.order.OrderFilter;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ForObtainingOrders {
    OrderDetailOutput findById(String id);
    Page<OrderSummaryOutput> filter(OrderFilter filter);
    OrderDetailOutput findByIdAndCustomerId(String id, UUID customerId);
}
