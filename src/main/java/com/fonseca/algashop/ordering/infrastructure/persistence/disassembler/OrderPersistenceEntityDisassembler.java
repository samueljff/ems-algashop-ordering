package com.fonseca.algashop.ordering.infrastructure.persistence.disassembler;

import com.fonseca.algashop.ordering.domain.model.entity.Order;
import com.fonseca.algashop.ordering.domain.model.entity.OrderStatus;
import com.fonseca.algashop.ordering.domain.model.entity.PaymentMethod;
import com.fonseca.algashop.ordering.domain.model.valueObject.*;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.CustomerId;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.OrderId;
import com.fonseca.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.fonseca.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.fonseca.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.fonseca.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(persistenceEntity.getId()))
                .customerId(new CustomerId(persistenceEntity.getCustomerId()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
                .status(OrderStatus.valueOf(persistenceEntity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()))
                .placedAt(persistenceEntity.getPlacedAt())
                .paidAt(persistenceEntity.getPaidAt())
                .canceledAt(persistenceEntity.getCanceledAt())
                .readyAt(persistenceEntity.getReadyAt())
                .billing(convertBillingEmbeddableToValueObject(persistenceEntity.getBilling()))
                .shipping(convertShippingEmbeddableToValueObject(persistenceEntity.getShipping()))
                .items(new HashSet<>())
                .version(persistenceEntity.getVersion())
                .build();
    }

    private Billing convertBillingEmbeddableToValueObject(BillingEmbeddable billingEmbeddable) {
        if (billingEmbeddable == null) {
            return null;
        }

        return Billing.builder()
                .fullName(new FullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName()))
                .document(new Document(billingEmbeddable.getDocument()))
                .phone(new Phone(billingEmbeddable.getPhone()))
                .email(new Email(billingEmbeddable.getEmail()))
                .address(convertAddressEmbeddableToValueObject(billingEmbeddable.getAddress()))
                .build();
    }

    private Shipping convertShippingEmbeddableToValueObject(ShippingEmbeddable shippingEmbeddable) {
        if (shippingEmbeddable == null) {
            return null;
        }

        return Shipping.builder()
                .cost(new Money(shippingEmbeddable.getCost()))
                .expectedDate(shippingEmbeddable.getExpectedDate())
                .address(convertAddressEmbeddableToValueObject(shippingEmbeddable.getAddress()))
                .recipient(convertRecipientEmbeddableToValueObject(shippingEmbeddable.getRecipient()))
                .build();
    }

    private Address convertAddressEmbeddableToValueObject(AddressEmbeddable addressEmbeddable) {
        if (addressEmbeddable == null) {
            return null;
        }

        return Address.builder()
                .street(addressEmbeddable.getStreet())
                .number(addressEmbeddable.getNumber())
                .complement(addressEmbeddable.getComplement())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }

    private Recipient convertRecipientEmbeddableToValueObject(RecipientEmbeddable recipientEmbeddable) {
        if (recipientEmbeddable == null) {
            return null;
        }

        return Recipient.builder()
                .fullName(new FullName(recipientEmbeddable.getFirstName(), recipientEmbeddable.getLastName()))
                .document(new Document(recipientEmbeddable.getDocument()))
                .phone(new Phone(recipientEmbeddable.getPhone()))
                .build();
    }
}
