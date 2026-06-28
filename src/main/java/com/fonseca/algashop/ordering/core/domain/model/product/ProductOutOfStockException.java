package com.fonseca.algashop.ordering.core.domain.model.product;

import com.fonseca.algashop.ordering.core.domain.model.DomainException;
import com.fonseca.algashop.ordering.core.domain.model.ErrorMessages;

public class ProductOutOfStockException extends DomainException {

    public ProductOutOfStockException(ProductId id) {
        super(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK, id));
    }
}
