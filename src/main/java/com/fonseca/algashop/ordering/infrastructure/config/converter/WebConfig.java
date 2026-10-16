package com.fonseca.algashop.ordering.infrastructure.config.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SortDirectionConverter sortDirectionConverter;
    private final OrderSortTypeConverter orderSortTypeConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(sortDirectionConverter);
        registry.addConverter(orderSortTypeConverter);
    }
}