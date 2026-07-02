package com.fonseca.algashop.ordering.core.ports.in.order;

public interface ForManagingOrders {
    void cancel(Long rawOrderId);
    void markAsPaid(Long rawOrderId);
    void markAsReady(Long rawOrderId);
}
