package com.fonseca.algashop.ordering.domain.model.entity;

import com.fonseca.algashop.ordering.domain.model.valueObject.Money;
import com.fonseca.algashop.ordering.domain.model.valueObject.Product;
import com.fonseca.algashop.ordering.domain.model.valueObject.ProductName;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.ProductId;

public class ProductTestDataBuilder {

    public static final ProductId DEFAULT_PRODUCT_ID = new ProductId();

    private ProductTestDataBuilder() {
    }

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                .id(new ProductId())
                .inStock(true)
                .name(new ProductName("Notebook X11"))
                .price(new Money("3000"));
    }

    public static Product.ProductBuilder aProductUnavailable() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Desktop FX9000"))
                .price(new Money("5000"))
                .inStock(false);
    }

    public static Product.ProductBuilder aProductAltRamMemory() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("4GB RAM"))
                .price(new Money("200"))
                .inStock(true);
    }

    public static Product.ProductBuilder aProductAltMousePad() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Mouse Pad"))
                .price(new Money("100"))
                .inStock(true);
    }
}
