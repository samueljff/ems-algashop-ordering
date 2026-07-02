package com.fonseca.algashop.ordering.core.application.checkout;

import com.fonseca.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import org.springframework.transaction.annotation.Transactional;

public interface ForBuyingWithShoppingCart {
    String checkout(CheckoutInput input);
}
