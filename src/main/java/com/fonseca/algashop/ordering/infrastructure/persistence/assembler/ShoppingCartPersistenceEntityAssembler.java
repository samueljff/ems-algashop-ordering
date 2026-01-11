package com.fonseca.algashop.ordering.infrastructure.persistence.assembler;

import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceEntityAssembler {

    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
        return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
    }

    public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity, ShoppingCart shoppingCart) {

        CustomerPersistenceEntity customer = customerPersistenceEntityRepository.getReferenceById(shoppingCart.customerId().value());
        Set<ShoppingCartItemPersistenceEntity> mergedItems = mergeItems(shoppingCartPersistenceEntity, shoppingCart);

        shoppingCartPersistenceEntity.setId(shoppingCart.id().value());
        shoppingCartPersistenceEntity.setCustomer(customer);
        shoppingCartPersistenceEntity.setTotalAmount(shoppingCart.totalAmount().value());
        shoppingCartPersistenceEntity.setTotalItems(shoppingCart.totalItems().value());
        shoppingCartPersistenceEntity.setCreatedAt(shoppingCart.createdAt());
        shoppingCartPersistenceEntity.replaceItems(mergedItems);
        return shoppingCartPersistenceEntity;
    }

    private ShoppingCartItemPersistenceEntity mergeItem(ShoppingCartItemPersistenceEntity shoppingCartItemPersistenceEntity, ShoppingCartItem shoppingCartItem) {
        shoppingCartItemPersistenceEntity.setId(shoppingCartItem.id().value());
        shoppingCartItemPersistenceEntity.setProductId(shoppingCartItem.productId().value());
        shoppingCartItemPersistenceEntity.setName(shoppingCartItem.name().value());
        shoppingCartItemPersistenceEntity.setPrice(shoppingCartItem.price().value());
        shoppingCartItemPersistenceEntity.setQuantity(shoppingCartItem.quantity().value());
        shoppingCartItemPersistenceEntity.setAvailable(shoppingCartItem.isAvailable());
        shoppingCartItemPersistenceEntity.setTotalAmount(shoppingCartItem.totalAmount().value());
        return shoppingCartItemPersistenceEntity;
    }

    private Set<ShoppingCartItemPersistenceEntity> mergeItems(ShoppingCartPersistenceEntity shoppingCartPersistence, ShoppingCart shoppingCart) {
        Set<ShoppingCartItem> domainItems = shoppingCart.items();

        // Se o domínio não tem itens, limpa tudo
        if (CollectionUtils.isEmpty(domainItems)) {
            return new HashSet<>();
        }

        Set<ShoppingCartItemPersistenceEntity> persistenceItems =
                Optional.ofNullable(shoppingCartPersistence.getItems()).orElseGet(HashSet::new);

        // Indexa itens existentes por ID
        Map<UUID, ShoppingCartItemPersistenceEntity> persistenceItemsMap =
                persistenceItems.stream()
                        .collect(Collectors.toMap(
                                ShoppingCartItemPersistenceEntity::getId,
                                Function.identity()
                        ));

        // IDs que devem permanecer após o merge
        Set<UUID> domainItemIds = domainItems.stream()
                .map(i -> i.id().value())
                .collect(Collectors.toSet());

        // Remove órfãos (itens que não existem mais no domínio)
        persistenceItems.removeIf(
                persistenceItem -> !domainItemIds.contains(persistenceItem.getId())
        );

        // Cria ou atualiza itens
        Set<ShoppingCartItemPersistenceEntity> mergedItems = domainItems.stream()
                .map(domainItem -> {
                    ShoppingCartItemPersistenceEntity persistenceItem = persistenceItemsMap.getOrDefault(
                            domainItem.id().value(),
                            new ShoppingCartItemPersistenceEntity()
                    );

                    mergeItem(persistenceItem, domainItem);

                    // Garante o vínculo com o agregado
                    persistenceItem.setShoppingCart(shoppingCartPersistence);

                    return persistenceItem;
                }).collect(Collectors.toSet());

        return mergedItems;
    }

    private ShoppingCartItemPersistenceEntity toShoppingCartItemEntity(ShoppingCartItem sourceOfItem) {
        return ShoppingCartItemPersistenceEntity.builder()
                .id(sourceOfItem.id().value())
                .shoppingCart(ShoppingCartPersistenceEntity.builder().id(sourceOfItem.shoppingCartId().value()).build())
                .productId(sourceOfItem.productId().value())
                .name(sourceOfItem.name().value())
                .price(sourceOfItem.price().value())
                .quantity(sourceOfItem.quantity().value())
                .available(sourceOfItem.isAvailable())
                .totalAmount(sourceOfItem.totalAmount().value())
                .build();
    }
}
