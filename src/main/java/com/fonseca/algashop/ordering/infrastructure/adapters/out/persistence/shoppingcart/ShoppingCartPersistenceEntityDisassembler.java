package com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartItem;
import com.fonseca.algashop.ordering.core.domain.model.commons.Money;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductName;
import com.fonseca.algashop.ordering.core.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartItemId;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductId;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartId;
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
