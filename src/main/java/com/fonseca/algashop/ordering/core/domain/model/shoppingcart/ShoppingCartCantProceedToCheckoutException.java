package com.fonseca.algashop.ordering.core.domain.model.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.DomainException;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {

    public ShoppingCartCantProceedToCheckoutException() {
    }

    public ShoppingCartCantProceedToCheckoutException(Throwable cause) {
        super(cause);
    }

    public ShoppingCartCantProceedToCheckoutException(String message) {
        super(message);
    }

    public ShoppingCartCantProceedToCheckoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
