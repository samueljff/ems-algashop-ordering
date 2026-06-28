package com.fonseca.algashop.ordering.core.domain.model.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.DomainException;
import com.fonseca.algashop.ordering.core.domain.model.ErrorMessages;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductId;

public class ShoppingCartItemIncompatibleProductException  extends DomainException {

    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
