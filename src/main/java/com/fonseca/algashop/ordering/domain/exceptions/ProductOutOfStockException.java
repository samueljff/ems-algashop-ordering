package com.fonseca.algashop.ordering.domain.exceptions;

import com.fonseca.algashop.ordering.domain.valueobjet.id.ProductId;

public class ProductOutOfStockException extends DomainException {

    public ProductOutOfStockException(ProductId id) {
        super(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK, id));
    }
}
