package com.fonseca.algashop.ordering.infrastructure.persistence.disassembler;

import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ShoppingCartPersistenceEntityDisassemblerTest {

    private final ShoppingCartPersistenceEntityDisassembler disassembler = new ShoppingCartPersistenceEntityDisassembler();

    @Test
    void shouldConvertShoppingCartPersistenceEntityToDomain() {
        //Given
        ShoppingCartPersistenceEntity cartPersistence = ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart().build();

        // WHEN
        ShoppingCart domainCart = disassembler.toDomainEntity(cartPersistence);

        // THEN
        assertThat(domainCart.id().value()).isEqualTo(cartPersistence.getId());
        assertThat(domainCart.customerId().value()).isEqualTo(cartPersistence.getCustomerId());
        assertThat(domainCart.totalAmount().value()).isEqualByComparingTo(BigDecimal.valueOf(1250));
        assertThat(domainCart.totalItems().value()).isEqualTo(3);
        assertThat(domainCart.createdAt()).isEqualTo(cartPersistence.getCreatedAt());
        assertThat(domainCart.items().size()).isEqualTo(cartPersistence.getItems().size());

    }

}