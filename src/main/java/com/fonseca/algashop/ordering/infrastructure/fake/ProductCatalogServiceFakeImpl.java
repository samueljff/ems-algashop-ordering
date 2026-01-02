package com.fonseca.algashop.ordering.infrastructure.fake;

import com.fonseca.algashop.ordering.domain.model.service.ProductCatalogService;
import com.fonseca.algashop.ordering.domain.model.valueObject.Money;
import com.fonseca.algashop.ordering.domain.model.valueObject.Product;
import com.fonseca.algashop.ordering.domain.model.valueObject.ProductName;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.ProductId;

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
