package com.fonseca.algashop.ordering.infrastructure.persistence.disassembler;

import com.fonseca.algashop.ordering.domain.model.entity.ShoppingCart;
import com.fonseca.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.fonseca.algashop.ordering.domain.model.valueObject.Money;
import com.fonseca.algashop.ordering.domain.model.valueObject.ProductName;
import com.fonseca.algashop.ordering.domain.model.valueObject.Quantity;
import com.fonseca.algashop.ordering.domain.model.valueObject.ShoppingCartItemId;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.CustomerId;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.ProductId;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.ShoppingCartId;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartPersistenceEntityDisassembler {

    public ShoppingCart toDomainEntity(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(shoppingCartPersistenceEntity.getId()))
                .customerId(new CustomerId(shoppingCartPersistenceEntity.getCustomerId()))
                .totalAmount(new Money(shoppingCartPersistenceEntity.getTotalAmount()))
                .createdAt(shoppingCartPersistenceEntity.getCreatedAt())
                .items(toDomainEntityItems(shoppingCartPersistenceEntity.getItems()))
                .totalItems(new Quantity(shoppingCartPersistenceEntity.getTotalItems()))
                .build();
    }

    private Set<ShoppingCartItem> toDomainEntityItems(Set<ShoppingCartItemPersistenceEntity> sourceOfItems) {
        return sourceOfItems.stream().map(this::toItemEntity).collect(Collectors.toSet());
    }

    private ShoppingCartItem toItemEntity(ShoppingCartItemPersistenceEntity shoppingCartItemPersistenceEntity) {
        return ShoppingCartItem.existing()
                .id(new ShoppingCartItemId(shoppingCartItemPersistenceEntity.getId()))
                .shoppingCartId(new ShoppingCartId(shoppingCartItemPersistenceEntity.getShoppingCartId()))
                .productId(new ProductId(shoppingCartItemPersistenceEntity.getProductId()))
                .productName(new ProductName(shoppingCartItemPersistenceEntity.getName()))
                .price(new Money(shoppingCartItemPersistenceEntity.getPrice()))
                .quantity(new Quantity(shoppingCartItemPersistenceEntity.getQuantity()))
                .available(shoppingCartItemPersistenceEntity.getAvailable())
                .totalAmount(new Money(shoppingCartItemPersistenceEntity.getTotalAmount()))
                .build();
    }
}
