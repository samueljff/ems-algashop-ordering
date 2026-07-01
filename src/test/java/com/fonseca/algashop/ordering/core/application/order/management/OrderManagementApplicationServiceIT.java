package com.fonseca.algashop.ordering.core.application.order.management;

import com.fonseca.algashop.ordering.core.application.AbstractApplicationIT;
import com.fonseca.algashop.ordering.core.application.customer.CustomerLoyaltyPointsApplicationService;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.core.domain.model.customer.Customers;
import com.fonseca.algashop.ordering.core.domain.model.order.*;
import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderCanceledEvent;
import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderPaidEvent;
import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderPlacedEvent;
import com.fonseca.algashop.ordering.core.domain.model.order.events.OrderReadyEvent;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.listener.order.OrderEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;
import java.util.UUID;

@Import(OrderEventListener.class)
class OrderManagementApplicationServiceIT extends AbstractApplicationIT {

    @Autowired
    private Orders orders;

    @Autowired
    private OrderManagementApplicationService orderManagementApplicationService;

    @Autowired
    private Customers customers;

    @MockitoSpyBean
    private OrderEventListener orderEventListener;

    @MockitoSpyBean
    CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

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

        Mockito.verify(orderEventListener).listen(Mockito.any(OrderCanceledEvent.class));
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

        Mockito.verify(orderEventListener).listen(Mockito.any(OrderPaidEvent.class));
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

        Mockito.verify(orderEventListener).listen(Mockito.any(OrderReadyEvent.class));
        Mockito.verify(customerLoyaltyPointsApplicationService).addLoyaltyPoints(
            Mockito.any(UUID.class),
            Mockito.any(String.class)
        );
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

     /* =========================
       markAsPlace
       ========================= */

    @Test
    void shouldMarkOrderAsPlaceSuccessfully() {
        Order order = OrderTestDataBuilder.anOrder()
            .status(OrderStatus.PLACED)
            .build();
        orders.add(order);

        Optional<Order> updatedOrder = orders.ofId(order.id());
        Assertions.assertThat(updatedOrder).isPresent();
        Assertions.assertThat(updatedOrder.get().status()).isEqualTo(OrderStatus.PLACED);
        Assertions.assertThat(updatedOrder.get().placedAt()).isNotNull();

        Mockito.verify(orderEventListener).listen(Mockito.any(OrderPlacedEvent.class));
    }

}