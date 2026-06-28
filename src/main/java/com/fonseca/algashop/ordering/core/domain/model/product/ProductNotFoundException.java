package com.fonseca.algashop.ordering.core.domain.model.product;

import com.fonseca.algashop.ordering.core.domain.model.DomainException;

import static com.fonseca.algashop.ordering.core.domain.model.ErrorMessages.ERROR_PRODUCT_NOT_FOUND;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException() {
    }

    public ProductNotFoundException(ProductId productId) {
        super(String.format(ERROR_PRODUCT_NOT_FOUND, productId));
    }
}
