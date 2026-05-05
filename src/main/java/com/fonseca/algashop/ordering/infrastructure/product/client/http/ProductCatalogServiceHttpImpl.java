package com.fonseca.algashop.ordering.infrastructure.product.client.http;

import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.product.ProductCatalogService;
import com.fonseca.algashop.ordering.domain.model.product.ProductId;
import com.fonseca.algashop.ordering.domain.model.product.ProductName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductCatalogServiceHttpImpl implements ProductCatalogService {

    private final ProductCatalogAPIClient productCatalogAPIClient;

    @Override
    public Optional<Product> ofId(ProductId productId) {
        ProductResponse productResponse = productCatalogAPIClient.getById(productId.value());
        return Optional.of(
            Product.builder()
                .id(new ProductId(productResponse.getId()))
                .name(new ProductName(productResponse.getName()))
                .price(new Money(productResponse.getSalePrice()))
                .inStock(productResponse.getInStock())
                .build()
        );
    }
}
