package com.fonseca.algashop.ordering.infrastructure.product.client.fake;

import com.fonseca.algashop.ordering.core.domain.model.product.ProductCatalogService;
import com.fonseca.algashop.ordering.core.domain.model.commons.Money;
import com.fonseca.algashop.ordering.core.domain.model.product.Product;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductName;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductId;

import java.util.Optional;

//@Component
//@Profile({"test", "it"})
public class ProductCatalogServiceFakeImpl implements ProductCatalogService {

    @Override
    public Optional<Product> ofId(ProductId productId) {
        Product product = Product.builder()
            .id(productId)
            .name(new ProductName("NoteBook"))
            .price(new Money("3000"))
            .inStock(true)
            .build();
        return Optional.of(product);
    }
}
