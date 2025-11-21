package com.fonseca.algashop.ordering.domain.entity;

import com.fonseca.algashop.ordering.domain.exceptions.OrderCannotBeEditedException;
import com.fonseca.algashop.ordering.domain.valueObject.Billing;
import com.fonseca.algashop.ordering.domain.valueObject.Product;
import com.fonseca.algashop.ordering.domain.valueObject.Quantity;
import com.fonseca.algashop.ordering.domain.valueObject.Shipping;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public class OrderChangingTest {

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

        ThrowableAssert.ThrowingCallable changePaymentTask = () -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

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

        ThrowableAssert.ThrowingCallable changePaymentTask = () -> readyOrder.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(changePaymentTask);
    }
}
