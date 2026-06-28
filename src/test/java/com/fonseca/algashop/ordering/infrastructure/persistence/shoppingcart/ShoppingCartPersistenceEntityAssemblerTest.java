package com.fonseca.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartItem;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ShoppingCartPersistenceEntityAssemblerTest {
    @Mock
    private CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    @InjectMocks
    private ShoppingCartPersistenceEntityAssembler assembler;

    @BeforeEach
    public void setup() {
        Mockito.when(customerPersistenceEntityRepository.getReferenceById(Mockito.any(UUID.class)))
                .then(a -> {
                    UUID customerId = a.getArgument(0, UUID.class);
                    return CustomerPersistenceEntityTestDataBuilder.aCustomer().id(customerId).build();
                });
    }

    @Test
    void shouldConvertToDomain() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCartPersistenceEntity persistence = assembler.fromDomain(cart);

        assertThat(persistence.getId()).isEqualTo(cart.id().value());
        assertThat(persistence.getCustomer().getId()).isEqualTo(cart.customerId().value());
        assertThat(persistence.getTotalAmount()).isEqualTo(cart.totalAmount().value());
        assertThat(persistence.getTotalItems()).isEqualTo(cart.totalItems().value());
        assertThat(persistence.getItems().size()).isEqualTo((cart.items().size()));
        // Cada item deve ter o vínculo com o agregado
        persistence.getItems().forEach(item -> assertThat(item.getShoppingCartId()).isEqualTo(persistence.getId()));

    }

    @Test
    void shouldReuseExistingPersistenceItemWhenMerging() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCartItem domainItem = cart.items().iterator().next();

        ShoppingCartPersistenceEntity existing = new ShoppingCartPersistenceEntity();

        ShoppingCartItemPersistenceEntity existingItem =
                new ShoppingCartItemPersistenceEntity();
        existingItem.setId(domainItem.id().value());
        existingItem.setShoppingCart(existing);

        existing.setItems(new HashSet<>());
        existing.getItems().add(existingItem);

        ShoppingCartPersistenceEntity merged = assembler.merge(existing, cart);

        ShoppingCartItemPersistenceEntity reusedItem =
                merged.getItems().stream()
                        .filter(i -> i.getId().equals(domainItem.id().value()))
                        .findFirst()
                        .orElseThrow();

        assertThat(reusedItem).isSameAs(existingItem);
    }

    @Test
    void shouldRemoveOrphanItemsWhenMerging() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        ShoppingCartPersistenceEntity persistence = new ShoppingCartPersistenceEntity();

        ShoppingCartItemPersistenceEntity orphanItem = new ShoppingCartItemPersistenceEntity();
        orphanItem.setId(UUID.randomUUID());

        persistence.setItems(new HashSet<>(Set.of(orphanItem)));

        ShoppingCartPersistenceEntity merged = assembler.merge(persistence, cart);

        assertThat(merged.getItems()).doesNotMatch(i -> i.getClass().equals(orphanItem.getId()));
    }
}