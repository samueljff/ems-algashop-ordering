package com.fonseca.algashop.ordering.application.order.query;

import com.fonseca.algashop.ordering.application.utility.SortablePageFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderFilter extends SortablePageFilter<OrderFilter.SortType> {

    private String status;
    private String orderId;
    private String customerId;
    private OffsetDateTime placedAtFrom;
    private OffsetDateTime placedAtTo;
    private BigDecimal totalAmountFrom;
    private BigDecimal totalAmountTo;

    @Override
    public SortType getSortByPropertyOrDefault() {
        return getSortByProperty() == null ? SortType.PLACE_AT : getSortByProperty();
    }

    @Override
    public Sort.Direction getSortDirectionOrDefault() {
        return getSortDirection() == null ? Sort.Direction.ASC : getSortDirection();
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        PLACE_AT("placedAt"),
        PAID_AT("paidAt"),
        CANCELED_AT("canceledAt"),
        READY_AT("readyAt"),
        STATUS("status");

        private final String propertyName;
    }
}
