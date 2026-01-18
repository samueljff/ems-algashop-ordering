package com.fonseca.algashop.ordering.application.commons;

import com.fonseca.algashop.ordering.domain.model.product.ProductCatalogService;
import com.fonseca.algashop.ordering.infrastructure.product.client.fake.ProductCatalogServiceFakeImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ProductCatalogServiceTestConfig {
    @Bean
    public ProductCatalogService productCatalogService() {
        return new ProductCatalogServiceFakeImpl();
    }
}
