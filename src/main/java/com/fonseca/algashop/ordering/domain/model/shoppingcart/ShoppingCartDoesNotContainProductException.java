package com.fonseca.algashop.ordering.domain.model.shoppingcart;

import com.fonseca.algashop.ordering.domain.model.DomainException;
import com.fonseca.algashop.ordering.domain.model.ErrorMessages;
import com.fonseca.algashop.ordering.domain.model.product.ProductId;

public class ShoppingCartDoesNotContainProductException extends DomainException {

    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT, id, productId));
    }
}
