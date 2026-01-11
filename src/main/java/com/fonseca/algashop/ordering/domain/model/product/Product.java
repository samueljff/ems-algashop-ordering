package com.fonseca.algashop.ordering.domain.model.product;

import com.fonseca.algashop.ordering.domain.model.commons.Money;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Product(ProductId id, ProductName name, Money price, Boolean inStock) {

    public Product {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(price);
        Objects.requireNonNull(inStock);
    }

    public void checkOutOfStock() {
        if (isOutOfStock()) {
            throw new ProductOutOfStockException(this.id());
        }
    }

    private boolean isOutOfStock() {
        return !inStock();
    }
}
