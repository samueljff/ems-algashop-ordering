package com.fonseca.algashop.ordering.domain.model.exceptions;

import com.fonseca.algashop.ordering.domain.model.valueObject.id.ProductId;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.ShoppingCartId;

public class ShoppingCartDoesNotContainProductException extends DomainException {

    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT, id, productId));
    }
}
