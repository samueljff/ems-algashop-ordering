package com.fonseca.algashop.ordering.infrastructure.config.converter;

import com.fonseca.algashop.ordering.core.ports.in.order.OrderFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class OrderSortTypeConverter implements Converter<String, OrderFilter.SortType> {
    @Override
    public OrderFilter.SortType convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            return OrderFilter.SortType.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // valor inválido -> cai no default via getSortByPropertyOrDefault()
        }
    }
}