package com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.AbstractPersistenceIT;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

@TestPropertySource(properties = "spring.flyway.locations=classpath:db/migration,classpath:db/testdata")
class ShoppingCartPersistenceEntityRepositoryIT extends AbstractPersistenceIT {

    private static final UUID validCustomerId = UUID.fromString("3a4b5c6d-7e8f-9a0b-1c2d-3e4f5a6b7c8d");
    private static final UUID validShoppingCartId = UUID.fromString("4f31582a-66e6-4601-a9d3-ff608c2d4461");

    private final ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository;
    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    @Autowired
    public ShoppingCartPersistenceEntityRepositoryIT(ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository,
                                                     CustomerPersistenceEntityRepository customerPersistenceEntityRepository) {
        this.shoppingCartPersistenceEntityRepository = shoppingCartPersistenceEntityRepository;
        this.customerPersistenceEntityRepository = customerPersistenceEntityRepository;
    }

    @Test
    @Sql(scripts = "classpath:db/clean/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/testdata/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldPersistShoppingCartWithItems() {
        CustomerPersistenceEntity customerPersistenceEntity = customerPersistenceEntityRepository
            .findById(validCustomerId).orElseThrow(() -> new CustomerNotFoundException());

        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart()
                .customer(customerPersistenceEntity)
                .build();

        Assertions.assertThat(entity.getItems()).isNotEmpty();

        shoppingCartPersistenceEntityRepository.saveAndFlush(entity);
        Assertions.assertThat(shoppingCartPersistenceEntityRepository.existsById(entity.getId())).isTrue();

        ShoppingCartPersistenceEntity savedEntity = shoppingCartPersistenceEntityRepository.findById(entity.getId()).orElseThrow();

        Assertions.assertThat(savedEntity.getCustomer().getId()).isEqualTo(validCustomerId);
        Assertions.assertThat(savedEntity.getItems()).allMatch(item -> item.getShoppingCartId().equals(entity.getId()));

        Assertions.assertThat(savedEntity.getItems()).isNotEmpty();
    }

    @Test
    public void shouldCount() {
        long shoppingCartsCount = shoppingCartPersistenceEntityRepository.count();

        ShoppingCartPersistenceEntity shoppingCartPersistenceEntity = shoppingCartPersistenceEntityRepository
            .findById(validShoppingCartId).orElseThrow(() -> new ShoppingCartNotFoundException());

        Assertions.assertThat(shoppingCartPersistenceEntityRepository.existsById(shoppingCartPersistenceEntity.getId())).isTrue();

        shoppingCartsCount = shoppingCartPersistenceEntityRepository.count();

        Assertions.assertThat(shoppingCartsCount).isEqualTo(2L);
    }

    @Test
    @Sql(scripts = "classpath:db/clean/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/testdata/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldAutomaticallySetAuditingFieldsOnPersist() {
        CustomerPersistenceEntity customerPersistenceEntity = customerPersistenceEntityRepository
            .findById(validCustomerId).orElseThrow(() -> new CustomerNotFoundException());
        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart()
                .customer(customerPersistenceEntity)
                .build();
        entity = shoppingCartPersistenceEntityRepository.saveAndFlush(entity);

        Assertions.assertThat(entity.getCreatedByUserId()).isNotNull();

        Assertions.assertThat(entity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedByUserId()).isNotNull();
    }

}
