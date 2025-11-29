package com.fonseca.algashop.ordering.domain.model.exceptions;

import com.fonseca.algashop.ordering.domain.model.valueObject.ShoppingCartItemId;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.ShoppingCartId;

public class ShoppingCartDoesNotContainItemException extends DomainException {
    public ShoppingCartDoesNotContainItemException(ShoppingCartId id, ShoppingCartItemId shoppingCartItemId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM, id, shoppingCartItemId));
    }
}
