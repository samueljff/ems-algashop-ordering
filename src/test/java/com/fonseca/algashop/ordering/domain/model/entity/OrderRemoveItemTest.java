package com.fonseca.algashop.ordering.domain.model.entity;

import com.fonseca.algashop.ordering.domain.model.exceptions.OrderCannotBeEditedException;
import com.fonseca.algashop.ordering.domain.model.exceptions.OrderDoesNotContainOrderItemException;
import com.fonseca.algashop.ordering.domain.model.valueObject.Money;
import com.fonseca.algashop.ordering.domain.model.valueObject.Product;
import com.fonseca.algashop.ordering.domain.model.valueObject.Quantity;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.CustomerId;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.OrderItemId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public class OrderRemoveItemTest {
    @Test
    public void givenDraftOrderWithTwoItems_whenRemoveOneItem_shouldRecalculateTotalsCorrectly() {
        // Given
        Order order = Order.draft(new CustomerId());

        Product mousePad = ProductTestDataBuilder.aProductAltMousePad().build(); // 100
        Product ramMemory = ProductTestDataBuilder.aProductAltRamMemory().build(); // 200

        order.addItem(mousePad, new Quantity(2)); // 200
        order.addItem(ramMemory, new Quantity(1)); // 200

        // Verificar estado inicial
        Assertions.assertThat(order.items()).hasSize(2);
        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("400"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(3));

        // Obter ID do primeiro item para remover
        OrderItem itemToRemove = order.items().stream()
                .filter(i -> {
                    return i.productId().equals(mousePad.id());
                })
                .findFirst()
                .orElseThrow();

        // When
        order.removeItem(itemToRemove.id());

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.items()).hasSize(1),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("200")),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(1))
        );
    }

    @Test
    public void givenDraftOrderWithSingleItem_whenRemoveItem_shouldHaveEmptyItemsAndZeroTotals() {
        // Given
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        order.addItem(product, new Quantity(2));

        OrderItemId itemId = order.items().iterator().next().id();

        // When
        order.removeItem(itemId);

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.items()).isEmpty(),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(Quantity.ZERO)
        );
    }

    @Test
    public void givenDraftOrder_whenTryToRemoveNonExistentItem_shouldThrowOrderDoesNotContainOrderItemException() {
        // Given
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        order.addItem(product, new Quantity(1));

        // ID de item que não existe no pedido
        OrderItemId nonExistentItemId = new OrderItemId();

        // When
        ThrowableAssert.ThrowingCallable removeItemTask = () -> order.removeItem(nonExistentItemId);

        // Then
        Assertions.assertThatExceptionOfType(OrderDoesNotContainOrderItemException.class)
                .isThrownBy(removeItemTask);
    }

    @Test
    public void givenPlacedOrder_whenTryToRemoveItem_shouldThrowOrderCannotBeEditedException() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();

        // Obter ID de um item existente no pedido
        OrderItemId itemId = order.items().iterator().next().id();

        // When
        ThrowableAssert.ThrowingCallable removeItemTask = () -> order.removeItem(itemId);

        // Then
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(removeItemTask);
    }

    @Test
    public void givenPaidOrder_whenTryToRemoveItem_shouldThrowOrderCannotBeEditedException() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        // Obter ID de um item existente no pedido
        OrderItemId itemId = order.items().iterator().next().id();

        // When
        ThrowableAssert.ThrowingCallable removeItemTask = () -> order.removeItem(itemId);

        // Then
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(removeItemTask);
    }

    @Test
    public void givenDraftOrder_whenTryToRemoveItemWithNullId_shouldThrowNullPointerException() {
        // Given
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        order.addItem(product, new Quantity(1));

        // When
        ThrowableAssert.ThrowingCallable removeItemTask = () -> order.removeItem(null);

        // Then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(removeItemTask);
    }

    @Test
    public void givenDraftOrderWithThreeItems_whenRemoveMiddleItem_shouldKeepOtherItemsIntact() {
        // Given
        Order order = Order.draft(new CustomerId());

        Product product1 = ProductTestDataBuilder.aProductAltMousePad().build(); // 100
        Product product2 = ProductTestDataBuilder.aProductAltRamMemory().build(); // 200
        Product product3 = ProductTestDataBuilder.aProduct().build(); // 3000

        order.addItem(product1, new Quantity(1)); // 100
        order.addItem(product2, new Quantity(1)); // 200
        order.addItem(product3, new Quantity(1)); // 3000

        // Verificar estado inicial
        Assertions.assertThat(order.items()).hasSize(3);
        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("3300"));

        // Obter ID do item do meio para remover
        OrderItem itemToRemove = order.items().stream()
                .filter(i -> i.productId().equals(product2.id()))
                .findFirst()
                .orElseThrow();

        // When
        order.removeItem(itemToRemove.id());

        // Then
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.items()).hasSize(2),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("3100")),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(2))
        );

        // Verificar que os outros itens ainda estão presentes
        boolean hasProduct1 = order.items().stream()
                .anyMatch(i -> i.productId().equals(product1.id()));
        boolean hasProduct3 = order.items().stream()
                .anyMatch(i -> i.productId().equals(product3.id()));

        Assertions.assertThat(hasProduct1).isTrue();
        Assertions.assertThat(hasProduct3).isTrue();
    }

    @Test
    public void givenDraftOrderWithShippingCost_whenRemoveItem_shouldRecalculateTotalIncludingShipping() {
        // Given
        Order order = Order.draft(new CustomerId());

        Product product1 = ProductTestDataBuilder.aProductAltMousePad().build(); // 100
        Product product2 = ProductTestDataBuilder.aProductAltRamMemory().build(); // 200
        order.changeShipping(OrderTestDataBuilder.aShipping()); //10

        order.addItem(product1, new Quantity(1)); // 100
        order.addItem(product2, new Quantity(1)); // 200

        // Verificar estado inicial: 300 (itens) + 10 (shipping) = 310
        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("310"));

        // Obter ID do primeiro item para remover
        OrderItem itemToRemove = order.items().stream()
                .filter(i -> i.productId().equals(product1.id()))
                .findFirst()
                .orElseThrow();

        // When
        order.removeItem(itemToRemove.id());

        // Then - 200 (item restante) + 10 (shipping) = 210
        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("210"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(1));
    }
}
