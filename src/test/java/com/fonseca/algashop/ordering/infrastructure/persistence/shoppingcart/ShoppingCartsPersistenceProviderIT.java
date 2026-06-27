package com.fonseca.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.infrastructure.persistence.AbstractPersistenceIT;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.fonseca.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@Import({
        ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
@TestPropertySource(properties = "spring.flyway.locations=db/migration,classpath:db/testdata")
class ShoppingCartsPersistenceProviderIT extends AbstractPersistenceIT {

    private static final UUID validShoppingCartId = UUID.fromString("4f31582a-66e6-4601-a9d3-ff608c2d4461");

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

    @Test
    public void shouldFindShoppingCartByCustomerId() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customersPersistenceProvider.add(customer);
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
            .customerId(customer.id())
            .build();
        persistenceProvider.add(shoppingCart);

        ShoppingCart foundCart = persistenceProvider.ofCustomer(customer.id()).orElseThrow();

        assertThat(foundCart).isNotNull();
        assertThat(foundCart.customerId()).isEqualTo(customer.id());
        assertThat(foundCart.id()).isEqualTo(shoppingCart.id());
    }

    @Test
    public void shouldPersistAndRetrieveShoppingCartById() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customersPersistenceProvider.add(customer);

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
            .customerId(customer.id())
            .build();
        assertThat(shoppingCart.version()).isNull();

        persistenceProvider.add(shoppingCart);

        assertThat(shoppingCart.version()).isNotNull().isEqualTo(0L);

        ShoppingCart cartSearch = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();
        assertThat(cartSearch).isNotNull();
        assertThat(cartSearch.id()).isEqualTo(shoppingCart.id());
        assertThat(cartSearch.totalItems().value()).isEqualTo(3);
    }

    @Test
    public void shouldCountShoppingCartsCorrectlyAfterAddingMultipleCarts() {
        long countBefore = persistenceProvider.count();

        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customersPersistenceProvider.add(customer);

        ShoppingCart cart1 = ShoppingCartTestDataBuilder.aShoppingCart()
            .customerId(customer.id())
            .build();
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
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customersPersistenceProvider.add(customer);
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
            .customerId(customer.id())
            .build();
        persistenceProvider.add(shoppingCart);
        assertThat(persistenceProvider.exists(shoppingCart.id())).isTrue();

        persistenceProvider.remove(shoppingCart.id());

        assertThat(persistenceProvider.exists(shoppingCart.id())).isFalse();
        assertThat(entityRepository.findById(shoppingCart.id().value())).isEmpty();
    }

    @Test
    public void shouldRemoveShoppingCartByEntity() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customersPersistenceProvider.add(customer);
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
            .customerId(customer.id())
            .build();
        persistenceProvider.add(shoppingCart);
        assertThat(persistenceProvider.exists(shoppingCart.id())).isTrue();

        persistenceProvider.remove(shoppingCart);

        assertThat(persistenceProvider.exists(shoppingCart.id())).isFalse();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldPersistAndRetrieveShoppingCartWithoutActiveTransaction() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customersPersistenceProvider.add(customer);
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
            .customerId(customer.id())
            .build();

        persistenceProvider.add(shoppingCart);

        assertThatNoException().isThrownBy(() -> {
            ShoppingCart foundCart = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();
            assertThat(foundCart).isNotNull();
        });
    }
}