package com.fonseca.algashop.ordering.core.ports.in.order;

import com.fonseca.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderSummaryOutput;
import org.springframework.data.domain.Page;

public interface ForQueryingOrders {
    OrderDetailOutput findById(String id);
    Page<OrderSummaryOutput> filter(OrderFilter filter);
}
