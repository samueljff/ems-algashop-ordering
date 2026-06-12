package com.fonseca.algashop.ordering.infrastructure.persistence.order;

import com.fonseca.algashop.ordering.domain.model.CreditCardId;
import com.fonseca.algashop.ordering.domain.model.order.Order;
import com.fonseca.algashop.ordering.domain.model.order.OrderItem;
import com.fonseca.algashop.ordering.domain.model.commons.Address;
import com.fonseca.algashop.ordering.domain.model.order.Billing;
import com.fonseca.algashop.ordering.domain.model.order.Recipient;
import com.fonseca.algashop.ordering.domain.model.order.Shipping;
import com.fonseca.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderPersistenceEntityAssembler {

    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public OrderPersistenceEntity fromDomain(Order order) {
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity.setId(order.id().value().toLong());
        orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
        orderPersistenceEntity.setTotalItems(order.totalItems().value());
        orderPersistenceEntity.setStatus(order.status().name());
        orderPersistenceEntity.setPaymentMethod(order.paymentMethod().name());
        orderPersistenceEntity.setPlacedAt(order.placedAt());
        orderPersistenceEntity.setPaidAt(order.paidAt());
        orderPersistenceEntity.setCanceledAt(order.canceledAt());
        orderPersistenceEntity.setReadyAt(order.readyAt());
        orderPersistenceEntity.setVersion(order.version());
        orderPersistenceEntity.setBilling(convertBillingToEmbeddable(order.billing()));
        orderPersistenceEntity.setShipping(convertShippingToEmbeddable(order.shipping()));

        if (order.creditCardId() != null) {
            orderPersistenceEntity.setCreditCardId(order.creditCardId().id());
        }

        Set<OrderItemPersistenceEntity> mergedItems = mergeItems(order, orderPersistenceEntity);
        orderPersistenceEntity.replaceItems(mergedItems);

        var customerPersistenceEntity = customerPersistenceEntityRepository.getReferenceById(order.customerId().value());
        orderPersistenceEntity.setCustomer(customerPersistenceEntity);

        orderPersistenceEntity.addEvents(order.domainEvents());

        return orderPersistenceEntity;
    }

    private Set<OrderItemPersistenceEntity> mergeItems(Order order, OrderPersistenceEntity orderPersistenceEntity) {
        Set<OrderItem> newOrUpdatedItems = order.items();
        if (newOrUpdatedItems == null || newOrUpdatedItems.isEmpty()) {
            return new HashSet<>();
        }

        Set<OrderItemPersistenceEntity> existingItems = orderPersistenceEntity.getItems();

        if (existingItems == null || existingItems.isEmpty()) {
            return newOrUpdatedItems.stream().map(orderItem -> fromDomain(orderItem)).collect(Collectors.toSet());
        }

        Map<Long, OrderItemPersistenceEntity> existingItemMap = existingItems.stream()
            .collect(Collectors.toMap(OrderItemPersistenceEntity::getId, item -> item));
        return newOrUpdatedItems.stream()
            .map(orderItem -> {
                OrderItemPersistenceEntity itemPersistence = existingItemMap.getOrDefault(
                    orderItem.id().value().toLong(), new OrderItemPersistenceEntity()
                );
                return merge(itemPersistence, orderItem);
            }).collect(Collectors.toSet());
    }

    public OrderItemPersistenceEntity fromDomain(OrderItem orderItem) {
        return merge(new OrderItemPersistenceEntity(), orderItem);
    }

    private OrderItemPersistenceEntity merge(OrderItemPersistenceEntity orderItemPersistenceEntity, OrderItem orderItem) {
        orderItemPersistenceEntity.setId(orderItem.id().value().toLong());
        orderItemPersistenceEntity.setProductId(orderItem.productId().value());
        orderItemPersistenceEntity.setProductName(orderItem.productName().value());
        orderItemPersistenceEntity.setPrice(orderItem.price().value());
        orderItemPersistenceEntity.setQuantity(orderItem.quantity().value());
        orderItemPersistenceEntity.setTotalAmount(orderItem.totalAmount().value());
        return orderItemPersistenceEntity;
    }

    private ShippingEmbeddable convertShippingToEmbeddable(Shipping shipping) {
        if (shipping == null) {
            return null;
        }
        var builder = ShippingEmbeddable.builder()
            .expectedDate(shipping.expectedDate())
            .cost(shipping.cost().value())
            .address(convertAddressToEmbeddable(shipping.address()))
            .recipient(convertRecipientToEmbeddable(shipping.recipient()))
            .build();
        return builder;
    }

    private BillingEmbeddable convertBillingToEmbeddable(Billing billing) {
        if (billing == null) {
            return null;
        }
        return BillingEmbeddable.builder()
            .firstName(billing.fullName().firstName())
            .lastName(billing.fullName().lastName())
            .document(billing.document().value())
            .phone(billing.phone().value())
            .email(billing.email().value())
            .address(convertAddressToEmbeddable(billing.address()))
            .build();
    }

    private AddressEmbeddable convertAddressToEmbeddable(Address address) {
        if (address == null) {
            return null;
        }
        return AddressEmbeddable.builder()
            .city(address.city())
            .state(address.state())
            .number(address.number())
            .street(address.street())
            .complement(address.complement())
            .neighborhood(address.neighborhood())
            .zipCode(address.zipCode().value())
            .build();
    }

    private RecipientEmbeddable convertRecipientToEmbeddable(Recipient recipient) {
        if (recipient == null) {
            return null;
        }

        return RecipientEmbeddable.builder()
            .firstName(recipient.fullName().firstName())
            .lastName(recipient.fullName().lastName())
            .document(recipient.document().value())
            .phone(recipient.phone().value())
            .build();
    }
}
