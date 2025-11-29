package com.fonseca.algashop.ordering.domain.model.exceptions;

import com.fonseca.algashop.ordering.domain.model.valueObject.ShoppingCartItemId;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.ProductId;

public class ShoppingCartItemIncompatibleProductException  extends DomainException {

    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
