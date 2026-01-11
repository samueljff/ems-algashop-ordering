package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderIsCanceledTest {

    @Test
    public void givenDraftOrder_whenCheckIsCanceled_shouldReturnFalse() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        // When
        boolean isCanceled = order.isCanceled();

        // Then
        Assertions.assertThat(isCanceled).isFalse();
        Assertions.assertThat(order.isDraft()).isTrue();
    }

    @Test
    public void givenPlacedOrder_whenCheckIsCanceled_shouldReturnFalse() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        // When
        boolean isCanceled = order.isCanceled();

        // Then
        Assertions.assertThat(isCanceled).isFalse();
        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    public void givenPaidOrder_whenCheckIsCanceled_shouldReturnFalse() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        // When
        boolean isCanceled = order.isCanceled();

        // Then
        Assertions.assertThat(isCanceled).isFalse();
        Assertions.assertThat(order.isPaid()).isTrue();
    }

    @Test
    public void givenReadyOrder_whenCheckIsCanceled_shouldReturnFalse() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        order.markAsReady();

        // When
        boolean isCanceled = order.isCanceled();

        // Then
        Assertions.assertThat(isCanceled).isFalse();
        Assertions.assertThat(order.isReady()).isTrue();
    }

    @Test
    public void givenCanceledOrder_whenCheckIsCanceled_shouldReturnTrue() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().build();
        order.cancel();

        // When
        boolean isCanceled = order.isCanceled();

        // Then
        Assertions.assertThat(isCanceled).isTrue();
        Assertions.assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    public void givenOrderCanceledFromDraft_whenCheckIsCanceled_shouldReturnTrue() {
        // Given
        Order order = Order.draft(new CustomerId());

        // Verificar estado inicial
        Assertions.assertThat(order.isCanceled()).isFalse();

        // When
        order.cancel();

        // Then
        Assertions.assertThat(order.isCanceled()).isTrue();
    }

    @Test
    public void givenOrderCanceledFromPlaced_whenCheckIsCanceled_shouldReturnTrue() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        // Verificar estado inicial
        Assertions.assertThat(order.isCanceled()).isFalse();

        // When
        order.cancel();

        // Then
        Assertions.assertThat(order.isCanceled()).isTrue();
    }

    @Test
    public void givenOrderCanceledFromPaid_whenCheckIsCanceled_shouldReturnTrue() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        // Verificar estado inicial
        Assertions.assertThat(order.isCanceled()).isFalse();

        // When
        order.cancel();

        // Then
        Assertions.assertThat(order.isCanceled()).isTrue();
    }

    @Test
    public void givenOrderCanceledFromReady_whenCheckIsCanceled_shouldReturnTrue() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        order.markAsReady();

        // Verificar estado inicial
        Assertions.assertThat(order.isCanceled()).isFalse();

        // When
        order.cancel();

        // Then
        Assertions.assertThat(order.isCanceled()).isTrue();
    }

    @Test
    public void givenCanceledOrder_whenCheckOtherStatusMethods_shouldAllReturnFalse() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build();

        // When & Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.isCanceled()).isTrue(),
                o -> Assertions.assertThat(o.isDraft()).isFalse(),
                o -> Assertions.assertThat(o.isPlaced()).isFalse(),
                o -> Assertions.assertThat(o.isPaid()).isFalse(),
                o -> Assertions.assertThat(o.isReady()).isFalse()
        );
    }
}
