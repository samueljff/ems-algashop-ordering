package com.fonseca.algashop.ordering.application.order.management;

import com.fonseca.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.customer.Customers;
import com.fonseca.algashop.ordering.domain.model.order.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
class OrderManagementApplicationServiceIT {

    @Autowired
    private Orders orders;

    @Autowired
    private OrderManagementApplicationService orderManagementApplicationService;

    @Autowired
    private Customers customers;

    @BeforeEach
    public void setup() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

     /* =========================
       cancel
       ========================= */

    @Test
    void shouldCancelOrderSuccessfully() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PLACED)
                .build();
        orders.add(order);

        orderManagementApplicationService.cancel(order.id().value().toLong());

        Optional<Order> updatedOrder = orders.ofId(order.id());
        Assertions.assertThat(updatedOrder).isPresent();
        Assertions.assertThat(updatedOrder.get().status()).isEqualTo(OrderStatus.CANCELED);
        Assertions.assertThat(updatedOrder.get().canceledAt()).isNotNull();
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenCancelingNonExistingOrderId() {
        Long nonExistingOrderId = 999L;
        Assertions.assertThatThrownBy(() ->
                orderManagementApplicationService.cancel(nonExistingOrderId)
        ).isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenCancellingAlreadyCanceledOrder() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.CANCELED)
                .build();
        orders.add(order);

        Assertions.assertThatThrownBy(() ->
                orderManagementApplicationService.cancel(order.id().value().toLong())
        ).isInstanceOf(OrderStatusCannotBeChangedException.class);
    }

    /* =========================
       markAsPaid
       ========================= */

    @Test
    void shouldMarkOrderAsPaidSuccessfully() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PLACED)
                .build();
        orders.add(order);

        orderManagementApplicationService.markAsPaid(order.id().value().toLong());

        Optional<Order> updatedOrder = orders.ofId(order.id());
        Assertions.assertThat(updatedOrder).isPresent();
        Assertions.assertThat(updatedOrder.get().status()).isEqualTo(OrderStatus.PAID);
        Assertions.assertThat(updatedOrder.get().paidAt()).isNotNull();
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenMarkingAsPaidNonExistingOrderId() {
        Long nonExistingOrderId = new OrderId().value().toLong();
        Assertions.assertThatThrownBy(() ->
                orderManagementApplicationService.markAsPaid(nonExistingOrderId)
        ).isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenStatusTransitionIsInvalid() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.CANCELED)
                .build();
        orders.add(order);

        Assertions.assertThatThrownBy(() ->
                orderManagementApplicationService.markAsPaid(order.id().value().toLong())
        ).isInstanceOf(OrderStatusCannotBeChangedException.class);
    }

    /* =========================
       markAsReady
       ========================= */

    @Test
    void shouldMarkOrderAsReadySuccessfully() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PAID)
                .build();
        orders.add(order);

        orderManagementApplicationService.markAsReady(order.id().value().toLong());

        Optional<Order> updatedOrder = orders.ofId(order.id());
        Assertions.assertThat(updatedOrder).isPresent();
        Assertions.assertThat(updatedOrder.get().status()).isEqualTo(OrderStatus.READY);
        Assertions.assertThat(updatedOrder.get().readyAt()).isNotNull();
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenMarkingAsReadyNonExistingOrderId() {
        Long nonExistingOrderId = 888L;
        Assertions.assertThatThrownBy(() ->
                orderManagementApplicationService.markAsReady(nonExistingOrderId)
        ).isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenMarkingDraftOrderAsReady() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PLACED)
                .build();
        orders.add(order);

        Assertions.assertThatThrownBy(() ->
                orderManagementApplicationService.markAsReady(order.id().value().toLong())
        ).isInstanceOf(OrderStatusCannotBeChangedException.class);
    }
}