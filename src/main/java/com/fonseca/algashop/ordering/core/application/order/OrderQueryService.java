package com.fonseca.algashop.ordering.core.application.order;

import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.core.ports.in.order.ForQueryingOrders;
import com.fonseca.algashop.ordering.core.ports.in.order.OrderFilter;
import com.fonseca.algashop.ordering.core.ports.out.order.ForObtainingOrders;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderDetailOutput;
import com.fonseca.algashop.ordering.core.ports.out.order.OrderSummaryOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderQueryService implements ForQueryingOrders {

    private final ForObtainingOrders forQueryingOrders;
    private final SecurityChecks securityCheck;

    @Override
    public OrderDetailOutput findById(String id) {
        OrderDetailOutput order = forQueryingOrders.findById(id);
        if (!canAccess(order)) {
            throw new AccessDeniedException("You don't have permission to access this order");
        }
        return order;
    }

    @Override
    public Page<OrderSummaryOutput> filter(OrderFilter filter) {
        if (securityCheck.isCustomer()) {
            filter.setCustomerId(securityCheck.getAuthenticatedUserId());
        }
        return forQueryingOrders.filter(filter);
    }

    private boolean canAccess(OrderDetailOutput order) {
        if (!securityCheck.isCustomer() && securityCheck.isAuthenticated()) {
            return true;
        }
        return securityCheck.isCustomer()
            && securityCheck.getAuthenticatedUserId().equals(order.getCustomer().getId());
    }
}
