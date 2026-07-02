package com.fonseca.algashop.ordering.infrastructure.persistence.order;

import com.fonseca.algashop.ordering.core.domain.model.order.Order;
import com.fonseca.algashop.ordering.core.domain.model.order.OrderStatus;
import com.fonseca.algashop.ordering.core.domain.model.order.PaymentMethod;
import com.fonseca.algashop.ordering.core.domain.model.order.BillingTestDataBuilder;
import com.fonseca.algashop.ordering.core.domain.model.commons.Money;
import com.fonseca.algashop.ordering.core.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.core.domain.model.order.shipping.ShippingTestDataBuilder;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.core.domain.model.order.OrderId;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.order.OrderPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.order.OrderPersistenceEntityDisassembler;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderPersistenceEntityDisassemblerTest {

    private final OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    @Test
    public void shouldConvertFromPersistence() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);
        assertThat(domainEntity).satisfies(
                s -> assertThat(s.id()).isEqualTo(new OrderId(persistenceEntity.getId())),
                s -> assertThat(s.customerId()).isEqualTo(new CustomerId(persistenceEntity.getCustomerId())),
                s -> assertThat(s.totalAmount()).isEqualTo(new Money(persistenceEntity.getTotalAmount())),
                s -> assertThat(s.totalItems()).isEqualTo(new Quantity(persistenceEntity.getTotalItems())),
                s -> assertThat(s.placedAt()).isEqualTo(persistenceEntity.getPlacedAt()),
                s -> assertThat(s.paidAt()).isEqualTo(persistenceEntity.getPaidAt()),
                s -> assertThat(s.canceledAt()).isEqualTo(persistenceEntity.getCanceledAt()),
                s -> assertThat(s.readyAt()).isEqualTo(persistenceEntity.getReadyAt()),
                s -> assertThat(s.status()).isEqualTo(OrderStatus.valueOf(persistenceEntity.getStatus())),
                s -> assertThat(s.paymentMethod()).isEqualTo(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod())),
                s -> assertThat(s.items().size()).isEqualTo(persistenceEntity.getItems().size())
        );
    }

    @Test
    void shouldConvertBillingEmbeddableToValueObjects() {
        // Given
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .billing(BillingTestDataBuilder.aBillingEmbeddable()).build();

        // When
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        // Then
        assertThat(domainEntity.billing()).isNotNull();
        assertThat(domainEntity.billing()).satisfies(
                b -> assertThat(b.fullName().firstName()).isEqualTo(persistenceEntity.getBilling().getFirstName()),
                b -> assertThat(b.fullName().lastName()).isEqualTo(persistenceEntity.getBilling().getLastName()),
                b -> assertThat(b.document().value()).isEqualTo(persistenceEntity.getBilling().getDocument()),
                b -> assertThat(b.phone().value()).isEqualTo(persistenceEntity.getBilling().getPhone()),
                b -> assertThat(b.address()).isNotNull()
        );
    }

    @Test
    void shouldConvertShippingEmbeddableToValueObjects() {
        // Given
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .shipping(ShippingTestDataBuilder.aShippingEmbeddable()).build();

        // When
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        // Then
        assertThat(domainEntity.shipping()).isNotNull();
        assertThat(domainEntity.shipping()).satisfies(
                s -> assertThat(s.cost().value()).isEqualByComparingTo(persistenceEntity.getShipping().getCost()),
                s -> assertThat(s.expectedDate()).isEqualTo(persistenceEntity.getShipping().getExpectedDate()),
                s -> assertThat(s.address()).isNotNull(),
                s -> assertThat(s.recipient()).isNotNull()
        );
    }

    @Test
    void shouldConvertBillingAddressEmbeddableToValueObjects() {
        // Given
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .billing(BillingTestDataBuilder.aBillingEmbeddable()).build();
        // When
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        // Then
        assertThat(domainEntity.billing().address()).satisfies(
                a -> assertThat(a.street()).isEqualTo(persistenceEntity.getBilling().getAddress().getStreet()),
                a -> assertThat(a.number()).isEqualTo(persistenceEntity.getBilling().getAddress().getNumber()),
                a -> assertThat(a.complement()).isEqualTo(persistenceEntity.getBilling().getAddress().getComplement()),
                a -> assertThat(a.neighborhood()).isEqualTo(persistenceEntity.getBilling().getAddress().getNeighborhood()),
                a -> assertThat(a.city()).isEqualTo(persistenceEntity.getBilling().getAddress().getCity()),
                a -> assertThat(a.state()).isEqualTo(persistenceEntity.getBilling().getAddress().getState()),
                a -> assertThat(a.zipCode().value()).isEqualTo(persistenceEntity.getBilling().getAddress().getZipCode())
        );
    }

    @Test
    void shouldConvertShippingAddressToValueObjects() {
        // Given
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .shipping(ShippingTestDataBuilder.aShippingEmbeddable()).build();

        // When
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        // Then
        assertThat(domainEntity.shipping().address()).satisfies(
                a -> assertThat(a.street()).isEqualTo(persistenceEntity.getShipping().getAddress().getStreet()),
                a -> assertThat(a.number()).isEqualTo(persistenceEntity.getShipping().getAddress().getNumber()),
                a -> assertThat(a.complement()).isEqualTo(persistenceEntity.getShipping().getAddress().getComplement()),
                a -> assertThat(a.neighborhood()).isEqualTo(persistenceEntity.getShipping().getAddress().getNeighborhood()),
                a -> assertThat(a.city()).isEqualTo(persistenceEntity.getShipping().getAddress().getCity()),
                a -> assertThat(a.state()).isEqualTo(persistenceEntity.getShipping().getAddress().getState()),
                a -> assertThat(a.zipCode().value()).isEqualTo(persistenceEntity.getShipping().getAddress().getZipCode())
        );
    }

    @Test
    void shouldConvertRecipientToValueObjects() {
        // Given
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .shipping(ShippingTestDataBuilder.aShippingEmbeddable()).build();
        // When
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        // Then
        assertThat(domainEntity.shipping().recipient()).satisfies(
                r -> assertThat(r.fullName().firstName()).isEqualTo(persistenceEntity.getShipping().getRecipient().getFirstName()),
                r -> assertThat(r.fullName().lastName()).isEqualTo(persistenceEntity.getShipping().getRecipient().getLastName()),
                r -> assertThat(r.document().value()).isEqualTo(persistenceEntity.getShipping().getRecipient().getDocument()),
                r -> assertThat(r.phone().value()).isEqualTo(persistenceEntity.getShipping().getRecipient().getPhone())
        );
    }

    @Test
    void shouldHandleNullBilling() {
        // Given
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .billing(null)
                .build();

        // When
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        // Then
        assertThat(domainEntity.billing()).isNull();
    }

    @Test
    void shouldHandleNullShipping() {
        // Given
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .shipping(null).build();

        // When
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        // Then
        assertThat(domainEntity.shipping()).isNull();
    }
}