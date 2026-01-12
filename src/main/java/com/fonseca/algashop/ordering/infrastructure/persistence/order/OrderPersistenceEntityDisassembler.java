package com.fonseca.algashop.ordering.infrastructure.persistence.order;

import com.fonseca.algashop.ordering.domain.model.commons.*;
import com.fonseca.algashop.ordering.domain.model.order.*;
import com.fonseca.algashop.ordering.domain.model.product.ProductName;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.domain.model.order.OrderId;
import com.fonseca.algashop.ordering.domain.model.order.OrderItemId;
import com.fonseca.algashop.ordering.domain.model.product.ProductId;
import com.fonseca.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

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
                .items(toDomainEntityItems(persistenceEntity.getItems()))
                .version(persistenceEntity.getVersion())
                .build();
    }

    private Set<OrderItem> toDomainEntityItems(Set<OrderItemPersistenceEntity> items) {
        return items.stream().map(i -> toDomainEntityItems(i)).collect(Collectors.toSet());
    }

   private OrderItem toDomainEntityItems(OrderItemPersistenceEntity persistenceEntity) {
        return OrderItem.existing()
                .id(new OrderItemId(persistenceEntity.getId()))
                .orderId(new OrderId(persistenceEntity.getOrderId()))
                .productId(new ProductId(persistenceEntity.getProductId()))
                .productName(new ProductName(persistenceEntity.getProductName()))
                .price(new Money(persistenceEntity.getPrice()))
                .quantity(new Quantity(persistenceEntity.getQuantity()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
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
