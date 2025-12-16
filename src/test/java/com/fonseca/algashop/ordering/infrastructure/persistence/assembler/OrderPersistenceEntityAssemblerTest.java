package com.fonseca.algashop.ordering.infrastructure.persistence.assembler;

import com.fonseca.algashop.ordering.domain.model.entity.Order;
import com.fonseca.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceEntityAssemblerTest {

    private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

    @Test
    void shouldConvertToDomain() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);
        assertThat(orderPersistenceEntity).satisfies(
                p-> assertThat(p.getId()).isEqualTo(order.id().value().toLong()),
                p-> assertThat(p.getCustomerId()).isEqualTo(order.customerId().value()),
                p -> assertThat(p.getTotalAmount()).isEqualTo(order.totalAmount().value()),
                p -> assertThat(p.getTotalItems()).isEqualTo(order.totalItems().value()),
                p -> assertThat(p.getStatus()).isEqualTo(order.status().name()),
                p -> assertThat(p.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
                p -> assertThat(p.getPlacedAt()).isEqualTo(order.placedAt()),
                p -> assertThat(p.getPaidAt()).isEqualTo(order.paidAt()),
                p -> assertThat(p.getCanceledAt()).isEqualTo(order.canceledAt()),
                p -> assertThat(p.getReadyAt()).isEqualTo(order.readyAt())
        );
    }

    @Test
    void shouldMerge() {

    }

    @Test
    void shouldConvertBillingToEmbeddable() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().build();

        // When
        OrderPersistenceEntity entity = assembler.fromDomain(order);

        // Then
        assertThat(entity.getBilling()).isNotNull();
        assertThat(entity.getBilling()).satisfies(
                b -> assertThat(b.getFirstName()).isEqualTo(order.billing().fullName().firstName()),
                b -> assertThat(b.getLastName()).isEqualTo(order.billing().fullName().lastName()),
                b -> assertThat(b.getDocument()).isEqualTo(order.billing().document().value()),
                b -> assertThat(b.getPhone()).isEqualTo(order.billing().phone().value()),
                b -> assertThat(b.getAddress()).isNotNull()
        );
    }

    @Test
    void shouldConvertBillingAddressToEmbeddable() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().build();

        // When
        OrderPersistenceEntity entity = assembler.fromDomain(order);

        // Then
        assertThat(entity.getBilling().getAddress()).satisfies(
                a -> assertThat(a.getStreet()).isEqualTo(order.billing().address().street()),
                a -> assertThat(a.getNumber()).isEqualTo(order.billing().address().number()),
                a -> assertThat(a.getComplement()).isEqualTo(order.billing().address().complement()),
                a -> assertThat(a.getNeighborhood()).isEqualTo(order.billing().address().neighborhood()),
                a -> assertThat(a.getCity()).isEqualTo(order.billing().address().city()),
                a -> assertThat(a.getState()).isEqualTo(order.billing().address().state()),
                a -> assertThat(a.getZipCode()).isEqualTo(order.billing().address().zipCode().value())
        );
    }

    @Test
    void shouldConvertShippingToEmbeddable() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().build();

        // When
        OrderPersistenceEntity entity = assembler.fromDomain(order);

        // Then
        assertThat(entity.getShipping()).isNotNull();
        assertThat(entity.getShipping()).satisfies(
                s -> assertThat(s.getCost()).isEqualByComparingTo(order.shipping().cost().value()),
                s -> assertThat(s.getExpectedDate()).isEqualTo(order.shipping().expectedDate()),
                s -> assertThat(s.getAddress()).isNotNull(),
                s -> assertThat(s.getRecipient()).isNotNull()
        );
    }

    @Test
    void shouldConvertShippingAddressToEmbeddable() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().build();
        // When
        OrderPersistenceEntity entity = assembler.fromDomain(order);

        // Then
        assertThat(entity.getShipping().getAddress()).satisfies(
                a -> assertThat(a.getStreet()).isEqualTo(order.shipping().address().street()),
                a -> assertThat(a.getNumber()).isEqualTo(order.shipping().address().number()),
                a -> assertThat(a.getComplement()).isEqualTo(order.shipping().address().complement()),
                a -> assertThat(a.getNeighborhood()).isEqualTo(order.shipping().address().neighborhood()),
                a -> assertThat(a.getCity()).isEqualTo(order.shipping().address().city()),
                a -> assertThat(a.getState()).isEqualTo(order.shipping().address().state()),
                a -> assertThat(a.getZipCode()).isEqualTo(order.shipping().address().zipCode().value())
        );
    }

    @Test
    void shouldConvertRecipientToEmbeddable() {
        // Given
        Order order = OrderTestDataBuilder.anOrder().build();

        // When
        OrderPersistenceEntity entity = assembler.fromDomain(order);

        // Then
        assertThat(entity.getShipping().getRecipient()).satisfies(
                r -> assertThat(r.getFirstName()).isEqualTo(order.shipping().recipient().fullName().firstName()),
                r -> assertThat(r.getLastName()).isEqualTo(order.shipping().recipient().fullName().lastName()),
                r -> assertThat(r.getDocument()).isEqualTo(order.shipping().recipient().document().value()),
                r -> assertThat(r.getPhone()).isEqualTo(order.shipping().recipient().phone().value())
        );
    }

}