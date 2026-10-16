package com.fonseca.algashop.ordering.infrastructure.config.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class SortDirectionConverter implements Converter<String, Sort.Direction> {
    @Override
    public Sort.Direction convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        return Sort.Direction.fromString(source.trim());
    }
}