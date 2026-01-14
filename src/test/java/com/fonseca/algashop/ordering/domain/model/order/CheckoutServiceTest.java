package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
        assertThat(order.items()).hasSize(expectedOrderItemsCount);

        assertThat(shoppingCart.isEmpty()).isTrue();
        assertThat(shoppingCart.totalAmount()).isEqualTo(Money.ZERO);
        assertThat(shoppingCart.totalItems()).isEqualTo(Quantity.ZERO);
    }

    @Test
    void givenShoppingCartWithUnavailableItems_whenCheckout_shouldThrowShoppingCartCantProceedToCheckoutException() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();

        Product product = ProductTestDataBuilder.aProduct().build();
        shoppingCart.addItem(product, new Quantity(1));

        Product product2 = ProductTestDataBuilder.aProductAltRamMemory().build();
        shoppingCart.addItem(product2, new Quantity(1));

        Product productAltUnavailable = ProductTestDataBuilder.aProductAltRamMemory().id(product2.id()).inStock(false).build();
        shoppingCart.refreshItem(productAltUnavailable);

        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> checkoutService.checkout(shoppingCart, billingInfo, shippingInfo, paymentMethod));

        assertThat(shoppingCart.isEmpty()).isFalse();
        assertThat(shoppingCart.items()).hasSize(2);
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
        assertThat(shoppingCart.items()).hasSize(2);
    }
}