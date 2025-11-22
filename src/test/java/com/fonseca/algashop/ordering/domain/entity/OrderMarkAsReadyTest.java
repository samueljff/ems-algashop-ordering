package com.fonseca.algashop.ordering.domain.entity;

import com.fonseca.algashop.ordering.domain.exceptions.OrderStatusCannotBeChangedException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public class OrderMarkAsReadyTest {

    @Test
    public void givenPaidOrder_whenMarkAsReady_shouldChangeStatusToReadyAndSetReadyAtTimestamp() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        // Verificar estado inicial
        Assertions.assertThat(order.isPaid()).isTrue();
        Assertions.assertThat(order.readyAt()).isNull();

        // When
        order.markAsReady();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.isReady()).isTrue(),
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.READY),
                o -> Assertions.assertThat(o.readyAt()).isNotNull()
        );
    }

    @Test
    public void givenDraftOrder_whenTryToMarkAsReady_shouldThrowOrderStatusCannotBeChangedException() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        // Verificar estado inicial
        Assertions.assertThat(order.isDraft()).isTrue();
        Assertions.assertThat(order.readyAt()).isNull();

        // When/Then
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> order.markAsReady());

        // Verificar que o estado não foi alterado
        Assertions.assertThat(order.isDraft()).isTrue();
        Assertions.assertThat(order.readyAt()).isNull();
    }

    @Test
    public void givenPlacedOrder_whenTryToMarkAsReady_shouldThrowOrderStatusCannotBeChangedException() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        // Verificar estado inicial
        Assertions.assertThat(order.isPlaced()).isTrue();
        Assertions.assertThat(order.readyAt()).isNull();

        // When
        ThrowableAssert.ThrowingCallable markAsReadyTask = order::markAsReady;

        // Then
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(markAsReadyTask);

        // Verificar que o estado não foi alterado
        Assertions.assertThat(order.isPlaced()).isTrue();
        Assertions.assertThat(order.readyAt()).isNull();
    }

    @Test
    public void givenReadyOrder_whenTryToMarkAsReadyAgain_shouldThrowOrderStatusCannotBeChangedException() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        order.markAsReady();

        // Verificar estado inicial
        Assertions.assertThat(order.isReady()).isTrue();
        Assertions.assertThat(order.readyAt()).isNotNull();

        // When
        ThrowableAssert.ThrowingCallable markAsReadyTask = order::markAsReady;

        // Then
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(markAsReadyTask);
    }

    @Test
    public void givenPaidOrder_whenMarkAsReady_shouldPreserveOtherTimestamps() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        // Capturar timestamps originais
        var originalPlacedAt = order.placedAt();
        var originalPaidAt = order.paidAt();

        // When
        order.markAsReady();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.placedAt()).isEqualTo(originalPlacedAt),
                o -> Assertions.assertThat(o.paidAt()).isEqualTo(originalPaidAt),
                o -> Assertions.assertThat(o.readyAt()).isNotNull(),
                o -> Assertions.assertThat(o.canceledAt()).isNull()
        );
    }

    @Test
    public void givenPaidOrder_whenMarkAsReady_shouldMaintainAllOrderDataUnchanged() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

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
        order.markAsReady();

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
                o -> Assertions.assertThat(o.isReady()).isTrue()
        );
    }

    @Test
    public void givenCompleteOrderLifecycle_whenMarkAsReadyAfterPaid_shouldFollowCorrectStatusProgression() {
        // Given - Criar pedido DRAFT
        Order order = OrderTestDataBuilder.anOrder().build();

        Assertions.assertThat(order.isDraft()).isTrue();

        // When - Transição DRAFT -> PLACED
        order.place();

        // Then
        Assertions.assertThat(order.isPlaced()).isTrue();
        Assertions.assertThat(order.placedAt()).isNotNull();

        // When - Transição PLACED -> PAID
        order.markAsPaid();

        // Then
        Assertions.assertThat(order.isPaid()).isTrue();
        Assertions.assertThat(order.paidAt()).isNotNull();

        // When - Transição PAID -> READY
        order.markAsReady();

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.isReady()).isTrue(),
                o -> Assertions.assertThat(o.readyAt()).isNotNull(),
                o -> Assertions.assertThat(o.placedAt()).isNotNull(),
                o -> Assertions.assertThat(o.paidAt()).isNotNull()
        );
    }
}
