package com.fonseca.algashop.ordering.core.domain.model.order;

import com.fonseca.algashop.ordering.core.domain.model.CreditCardId;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import com.fonseca.algashop.ordering.core.domain.model.product.Product;
import com.fonseca.algashop.ordering.core.domain.model.commons.Quantity;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

public class OrderChangingTest {

    @Test
    void givenDraftOrder_whenChangeIsPerformed_shouldNotThrowException() {
        Order draftOrder = OrderTestDataBuilder.anOrder().build();

        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        Quantity quantity = new Quantity(2);
        Billing billing = OrderTestDataBuilder.aBilling();
        Shipping shipping = OrderTestDataBuilder.aShipping();
        PaymentMethod method = PaymentMethod.CREDIT_CARD;
        CreditCardId creditCardId = new CreditCardId();

        OrderItem orderItem = draftOrder.items().iterator().next();

        assertThatCode(() -> draftOrder.addItem(product, quantity)).doesNotThrowAnyException();
        assertThatCode(() -> draftOrder.changeBilling(billing)).doesNotThrowAnyException();
        assertThatCode(() -> draftOrder.changeShipping(shipping)).doesNotThrowAnyException();
        assertThatCode(() -> draftOrder.changeItemQuantity(orderItem.id(), quantity)).doesNotThrowAnyException();
        assertThatCode(() -> draftOrder.changePaymentMethod(method, creditCardId)).doesNotThrowAnyException();
    }

    @Test
    public void givenPlacedOrder_whenTryToAddItem_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(product, new Quantity(1));

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(addItemTask);
    }

    @Test
    public void givenPlacedOrder_whenTryToChangeBilling_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Billing billing = OrderTestDataBuilder.aBilling();

        ThrowableAssert.ThrowingCallable changeBillingTask = () -> order.changeBilling(billing);

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(changeBillingTask);
    }

    @Test
    public void givenPlacedOrder_whenTryToChangeShipping_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Shipping shipping = OrderTestDataBuilder.aShipping();

        ThrowableAssert.ThrowingCallable changeShippingTask = () -> order.changeShipping(shipping);

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(changeShippingTask);
    }

    @Test
    public void givenPlacedOrder_whenTryToChangePaymentMethod_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        ThrowableAssert.ThrowingCallable changePaymentTask = () -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD, new CreditCardId());

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(changePaymentTask);
    }

    @Test
    public void givenPlacedOrder_whenTryToChangeItemQuantity_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        // Assumindo que o pedido tem pelo menos um item
        OrderItem orderItem = order.items().iterator().next();

        ThrowableAssert.ThrowingCallable changeQuantityTask = () ->
                order.changeItemQuantity(orderItem.id(), new Quantity(10));

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(changeQuantityTask);
    }

    @Test
    public void givenPaidOrder_whenTryToAddItem_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(product, new Quantity(1));

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(addItemTask);
    }

    @Test
    public void givenCanceledOrder_whenTryToChangeBilling_shouldNotAllow() {
        // Criar pedido e fazer transição válida para CANCELED (a partir de PLACED)
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        // Simular cancelamento mudando o status manualmente via builder existing
        Order canceledOrder = Order.existing()
                .id(order.id())
                .customerId(order.customerId())
                .totalAmount(order.totalAmount())
                .totalItems(order.totalItems())
                .placedAt(order.placedAt())
                .paidAt(null)
                .canceledAt(java.time.OffsetDateTime.now())
                .readyAt(null)
                .billing(order.billing())
                .shipping(order.shipping())
                .status(OrderStatus.CANCELED)
                .paymentMethod(order.paymentMethod())
                .items(new java.util.HashSet<>(order.items()))
                .build();

        Billing billing = OrderTestDataBuilder.aBilling();

        ThrowableAssert.ThrowingCallable changeBillingTask = () -> canceledOrder.changeBilling(billing);

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(changeBillingTask);
    }

    @Test
    public void givenReadyOrder_whenTryToChangePaymentMethod_shouldNotAllow() {
        // Criar pedido e fazer transição válida para READY (a partir de PAID)
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        // Simular transição para READY via builder existing
        Order readyOrder = Order.existing()
                .id(order.id())
                .customerId(order.customerId())
                .totalAmount(order.totalAmount())
                .totalItems(order.totalItems())
                .placedAt(order.placedAt())
                .paidAt(order.paidAt())
                .canceledAt(null)
                .readyAt(java.time.OffsetDateTime.now())
                .billing(order.billing())
                .shipping(order.shipping())
                .status(OrderStatus.READY)
                .paymentMethod(order.paymentMethod())
                .items(new java.util.HashSet<>(order.items()))
                .build();

        ThrowableAssert.ThrowingCallable changePaymentTask = () -> readyOrder.changePaymentMethod(PaymentMethod.CREDIT_CARD, new CreditCardId());

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(changePaymentTask);
    }
}
