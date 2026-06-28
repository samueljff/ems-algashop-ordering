package com.fonseca.algashop.ordering.core.domain.model.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.DomainException;
import com.fonseca.algashop.ordering.core.domain.model.ErrorMessages;

public class ShoppingCartDoesNotContainItemException extends DomainException {
    public ShoppingCartDoesNotContainItemException(ShoppingCartId id, ShoppingCartItemId shoppingCartItemId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM, id, shoppingCartItemId));
    }
}
