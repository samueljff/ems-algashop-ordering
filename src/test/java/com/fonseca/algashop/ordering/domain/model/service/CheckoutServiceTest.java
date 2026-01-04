package com.fonseca.algashop.ordering.domain.model.service;

import com.fonseca.algashop.ordering.domain.model.entity.*;
import com.fonseca.algashop.ordering.domain.model.exceptions.ShoppingCartCantProceedToCheckoutException;
import com.fonseca.algashop.ordering.domain.model.valueObject.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class CheckoutServiceTest {

    private final CheckoutService checkoutService = new CheckoutService();

    @Test
    void givenValidShoppingCart_whenCheckout_shouldReturnPlacedOrderAndEmptyShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(ShoppingCartTestDataBuilder.aShoppingCart().customerId);
        shoppingCart.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));

        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Money shoppingCartTotalAmount = shoppingCart.totalAmount();
        Quantity expectedOrderTotalItems = shoppingCart.totalItems();
        int expectedOrderItemsCount = shoppingCart.items().size();

        Order order = checkoutService.checkout(shoppingCart, billingInfo, shippingInfo, paymentMethod);

        assertThat(order).isNotNull();
        assertThat(order.id()).isNotNull();
        assertThat(order.customerId()).isEqualTo(shoppingCart.customerId());
        assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.billing()).isEqualTo(billingInfo);
        assertThat(order.shipping()).isEqualTo(shippingInfo);
        assertThat(order.isPlaced()).isTrue();

        Money expectedTotalAmountWithShipping = shoppingCartTotalAmount.add(shippingInfo.cost());
        assertThat(order.totalAmount()).isEqualTo(expectedTotalAmountWithShipping);
        assertThat(order.totalItems()).isEqualTo(expectedOrderTotalItems);
        assertThat(order.items().size()).isEqualTo(expectedOrderItemsCount);

        assertThat(shoppingCart.isEmpty()).isTrue();
        assertThat(shoppingCart.totalAmount()).isEqualTo(Money.ZERO);
        assertThat(shoppingCart.totalItems()).isEqualTo(Quantity.ZERO);
    }

    @Test
    void givenShoppingCartWithUnavailableItems_whenCheckout_shouldThrowShoppingCartCantProceedToCheckoutException() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();

        Product product1 = ProductTestDataBuilder.aProduct().build();
        shoppingCart.addItem(product1, new Quantity(2));
        Product product2 = ProductTestDataBuilder.aProductAltRamMemory().build();
        shoppingCart.addItem(product2, new Quantity(1));

        assertThat(shoppingCart.items()).isNotNull();

        ShoppingCartItem shoppingCartItem = shoppingCart.findItem(product1.id());
        ShoppingCartItem shoppingCartItem2 = shoppingCart.findItem(product2.id());

        assertThat(shoppingCart.items().isEmpty());

        shoppingCart.removeItem(shoppingCartItem.id());
        shoppingCart.removeItem(shoppingCartItem2.id());

        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> checkoutService.checkout(shoppingCart, billingInfo, shippingInfo, paymentMethod));

        assertThat(shoppingCart.items().size()).isEqualTo(0);
    }

    @Test
    void givenEmptyShoppingCart_whenCheckout_shouldThrowShoppingCartCantProceedToCheckoutException() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> checkoutService.checkout(shoppingCart, billingInfo, shippingInfo, paymentMethod));

        assertThat(shoppingCart.isEmpty()).isTrue();
    }

    @Test
    void givenShoppingCartWithUnavailableItems_whenCheckout_shouldNotModifyShoppingCartState() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(ShoppingCartTestDataBuilder.aShoppingCart().customerId);
        Product productInStock = ProductTestDataBuilder.aProduct().build();
        shoppingCart.addItem(productInStock, new Quantity(2));

        Money initialTotalAmount = shoppingCart.totalAmount();
        Quantity initialTotalItems = shoppingCart.totalItems();

        Product productAlt = ProductTestDataBuilder.aProductAltRamMemory().build();
        shoppingCart.addItem(productAlt, new Quantity(1));

        Product productAltUnavailable = ProductTestDataBuilder.aProductAltRamMemory().id(productAlt.id()).inStock(false).build();
        shoppingCart.refreshItem(productAltUnavailable);

        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> checkoutService.checkout(shoppingCart, billingInfo, shippingInfo, paymentMethod));

        assertThat(shoppingCart.isEmpty()).isFalse();

        Money expectedTotalAmount = productInStock.price()
                .multiply(new Quantity(2)).add(productAlt.price());
        assertThat(shoppingCart.totalAmount()).isEqualTo(expectedTotalAmount);
        assertThat(shoppingCart.totalItems()).isEqualTo(new Quantity(3));
        assertThat(shoppingCart.items().size()).isEqualTo(2);
    }
}