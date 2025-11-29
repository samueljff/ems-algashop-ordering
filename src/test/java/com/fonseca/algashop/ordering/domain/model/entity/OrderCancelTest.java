package com.fonseca.algashop.ordering.domain.model.entity;

import com.fonseca.algashop.ordering.domain.model.exceptions.OrderStatusCannotBeChangedException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public class OrderCancelTest {

    @Test
    public void givenDraftOrder_whenCancel_shouldChangeStatusToCanceledAndSetCanceledAtTimestamp() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        // Verificar estado inicial
        Assertions.assertThat(order.isDraft()).isTrue();
        Assertions.assertThat(order.canceledAt()).isNull();

        // When
        order.cancel();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.isCanceled()).isTrue(),
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    public void givenPlacedOrder_whenCancel_shouldChangeStatusToCanceledAndSetCanceledAtTimestamp() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        // Verificar estado inicial
        Assertions.assertThat(order.isPlaced()).isTrue();
        Assertions.assertThat(order.canceledAt()).isNull();

        // When
        order.cancel();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.isCanceled()).isTrue(),
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    public void givenPaidOrder_whenCancel_shouldChangeStatusToCanceledAndSetCanceledAtTimestamp() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        // Verificar estado inicial
        Assertions.assertThat(order.isPaid()).isTrue();
        Assertions.assertThat(order.canceledAt()).isNull();

        // When
        order.cancel();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.isCanceled()).isTrue(),
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    public void givenReadyOrder_whenCancel_shouldChangeStatusToCanceledAndSetCanceledAtTimestamp() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        order.markAsReady();

        // Verificar estado inicial
        Assertions.assertThat(order.isReady()).isTrue();
        Assertions.assertThat(order.canceledAt()).isNull();

        // When
        order.cancel();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.isCanceled()).isTrue(),
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    public void givenCanceledOrder_whenTryToCancelAgain_shouldThrowOrderStatusCannotBeChangedException() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build();

        // Capturar estado após primeiro cancelamento
        var originalCanceledAt = order.canceledAt();
        var originalStatus = order.status();

        // Verificar estado inicial
        Assertions.assertThat(order.isCanceled()).isTrue();
        Assertions.assertThat(order.canceledAt()).isNotNull();

        // When
        ThrowableAssert.ThrowingCallable cancelTask = order::cancel;

        // Then
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(cancelTask);

        // Verificar que o estado não foi alterado
        Assertions.assertThat(order.status()).isEqualTo(originalStatus);
        Assertions.assertThat(order.canceledAt()).isEqualTo(originalCanceledAt);
    }

    @Test
    public void givenPlacedOrder_whenCancel_shouldPreserveOtherTimestamps() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        // Capturar timestamps originais
        var originalPlacedAt = order.placedAt();

        // When
        order.cancel();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.placedAt()).isEqualTo(originalPlacedAt),
                o -> Assertions.assertThat(o.paidAt()).isNull(),
                o -> Assertions.assertThat(o.readyAt()).isNull(),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    public void givenPaidOrder_whenCancel_shouldPreserveAllPreviousTimestamps() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        // Capturar timestamps originais
        var originalPlacedAt = order.placedAt();
        var originalPaidAt = order.paidAt();

        // When
        order.cancel();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.placedAt()).isEqualTo(originalPlacedAt),
                o -> Assertions.assertThat(o.paidAt()).isEqualTo(originalPaidAt),
                o -> Assertions.assertThat(o.readyAt()).isNull(),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    public void givenReadyOrder_whenCancel_shouldPreserveAllPreviousTimestamps() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        order.markAsReady();

        // Capturar timestamps originais
        var originalPlacedAt = order.placedAt();
        var originalPaidAt = order.paidAt();
        var originalReadyAt = order.readyAt();

        // When
        order.cancel();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.placedAt()).isEqualTo(originalPlacedAt),
                o -> Assertions.assertThat(o.paidAt()).isEqualTo(originalPaidAt),
                o -> Assertions.assertThat(o.readyAt()).isEqualTo(originalReadyAt),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    public void givenDraftOrder_whenCancel_shouldMaintainAllOrderDataUnchanged() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        // Capturar dados originais
        var originalId = order.id();
        var originalCustomerId = order.customerId();
        var originalTotalAmount = order.totalAmount();
        var originalTotalItems = order.totalItems();
        var originalBilling = order.billing();
        var originalShipping = order.shipping();
        var originalPaymentMethod = order.paymentMethod();
        var originalItemsSize = order.items().size();

        // When
        order.cancel();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.id()).isEqualTo(originalId),
                o -> Assertions.assertThat(o.customerId()).isEqualTo(originalCustomerId),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(originalTotalAmount),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(originalTotalItems),
                o -> Assertions.assertThat(o.billing()).isEqualTo(originalBilling),
                o -> Assertions.assertThat(o.shipping()).isEqualTo(originalShipping),
                o -> Assertions.assertThat(o.paymentMethod()).isEqualTo(originalPaymentMethod),
                o -> Assertions.assertThat(o.items()).hasSize(originalItemsSize),
                o -> Assertions.assertThat(o.isCanceled()).isTrue()
        );
    }

    @Test
    public void givenOrderInEachLifecycleStage_whenCancel_shouldAlwaysSucceedExceptWhenAlreadyCanceled() {
        // Given - DRAFT
        Order draftOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        // When
        draftOrder.cancel();

        // Then
        Assertions.assertThat(draftOrder.isCanceled()).isTrue();

        // Given - PLACED
        Order placedOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        // When
        placedOrder.cancel();

        // Then
        Assertions.assertThat(placedOrder.isCanceled()).isTrue();

        // Given - PAID
        Order paidOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        // When
        paidOrder.cancel();

        // Then
        Assertions.assertThat(paidOrder.isCanceled()).isTrue();

        // Given - READY
        Order readyOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        readyOrder.markAsReady();

        // When
        readyOrder.cancel();

        // Then
        Assertions.assertThat(readyOrder.isCanceled()).isTrue();
    }
}
