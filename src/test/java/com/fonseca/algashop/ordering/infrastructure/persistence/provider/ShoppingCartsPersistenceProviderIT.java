package com.fonseca.algashop.ordering.infrastructure.persistence.provider;

import com.fonseca.algashop.ordering.domain.model.entity.Customer;
import com.fonseca.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.entity.ShoppingCart;
import com.fonseca.algashop.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.valueObject.id.CustomerId;
import com.fonseca.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.fonseca.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.fonseca.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.fonseca.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.fonseca.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.fonseca.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class ShoppingCartsPersistenceProviderIT {

    private ShoppingCartsPersistenceProvider persistenceProvider;
    private CustomersPersistenceProvider customersPersistenceProvider;
    private ShoppingCartPersistenceEntityRepository entityRepository;

    @Autowired
    public ShoppingCartsPersistenceProviderIT(ShoppingCartsPersistenceProvider persistenceProvider,
                                              CustomersPersistenceProvider customersPersistenceProvider,
                                              ShoppingCartPersistenceEntityRepository entityRepository) {
        this.persistenceProvider = persistenceProvider;
        this.customersPersistenceProvider = customersPersistenceProvider;
        this.entityRepository = entityRepository;
    }

    @BeforeEach
    public void setup() {
        entityRepository.deleteAll();
        if (!customersPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customersPersistenceProvider.add(
                    CustomerTestDataBuilder.existingCustomer().build()
            );
        }
    }

    @Test
    public void shouldPersistAndRetrieveShoppingCartById() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        assertThat(shoppingCart.version()).isNull();

        persistenceProvider.add(shoppingCart);

        assertThat(shoppingCart.version()).isNotNull().isEqualTo(0L);

        ShoppingCart cartSearch = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();
        assertThat(cartSearch).isNotNull();
        assertThat(cartSearch.id()).isEqualTo(shoppingCart.id());
        assertThat(cartSearch.totalItems().value()).isEqualTo(3);
    }

    @Test
    public void shouldFindShoppingCartByCustomerId() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
                .build();
        persistenceProvider.add(shoppingCart);

        ShoppingCart foundCart = persistenceProvider.ofCustomer(shoppingCart.customerId()).orElseThrow();

        assertThat(foundCart).isNotNull();
        assertThat(foundCart.customerId()).isEqualTo(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID);
        assertThat(foundCart.id()).isEqualTo(shoppingCart.id());
    }

    @Test
    public void shouldCountShoppingCartsCorrectlyAfterAddingMultipleCarts() {
        long countBefore = persistenceProvider.count();

        ShoppingCart cart1 = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceProvider.add(cart1);

        Customer otherCustomer = CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).build();
        customersPersistenceProvider.add(otherCustomer);

        ShoppingCart cart2 = ShoppingCartTestDataBuilder.aShoppingCart().customerId(otherCustomer.id()).build();
        persistenceProvider.add(cart2);

        long countAfter = persistenceProvider.count();

        assertThat(countAfter ).isEqualTo(countBefore + 2);
    }

    @Test
    public void shouldRemoveShoppingCartById() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceProvider.add(shoppingCart);
        assertThat(persistenceProvider.exists(shoppingCart.id())).isTrue();

        persistenceProvider.remove(shoppingCart.id());

        assertThat(persistenceProvider.exists(shoppingCart.id())).isFalse();
        assertThat(entityRepository.findById(shoppingCart.id().value())).isEmpty();
    }

    @Test
    public void shouldRemoveShoppingCartByEntity() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceProvider.add(shoppingCart);
        assertThat(persistenceProvider.exists(shoppingCart.id())).isTrue();

        persistenceProvider.remove(shoppingCart);

        assertThat(persistenceProvider.exists(shoppingCart.id())).isFalse();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldPersistAndRetrieveShoppingCartWithoutActiveTransaction() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        persistenceProvider.add(shoppingCart);

        assertThatNoException().isThrownBy(() -> {
            ShoppingCart foundCart = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();
            assertThat(foundCart).isNotNull();
        });
    }
}