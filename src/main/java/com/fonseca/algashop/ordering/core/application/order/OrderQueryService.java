package com.fonseca.algashop.ordering.core.application.order;

import com.fonseca.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.fonseca.algashop.ordering.core.ports.in.order.OrderFilter;
import com.fonseca.algashop.ordering.core.ports.out.order.ForObtainingOrders;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderSummaryOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderQueryService implements ForQueryingOrders {

    private final ForObtainingOrders forQueryingOrders;

    @Override
    public OrderDetailOutput findById(String id) {
        return forQueryingOrders.findById(id);
    }

    @Override
    public Page<OrderSummaryOutput> filter(OrderFilter filter) {
        return forQueryingOrders.filter(filter);
    }
}
