package com.fonseca.algashop.ordering.infrastructure.product.client.fake;

import com.fonseca.algashop.ordering.domain.model.product.ProductCatalogService;
import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.product.ProductName;
import com.fonseca.algashop.ordering.domain.model.product.ProductId;

import java.util.Optional;

public class ProductCatalogServiceFakeImpl implements ProductCatalogService {

    @Override
    public Optional<Product> ofId(ProductId productId) {
        Product product = Product.builder()
                .id(productId)
                .name(new ProductName("NoteBook"))
                .price(new Money("3000"))
                .build();
        return Optional.of(product);
    }
}
