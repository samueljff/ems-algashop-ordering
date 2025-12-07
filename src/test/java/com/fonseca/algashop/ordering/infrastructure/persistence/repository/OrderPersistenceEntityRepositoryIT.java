package com.fonseca.algashop.ordering.infrastructure.persistence.repository;

import com.fonseca.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderPersistenceEntityRepositoryIT {

    private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;

    @Autowired
    public OrderPersistenceEntityRepositoryIT(OrderPersistenceEntityRepository orderPersistenceEntityRepository) {
        this.orderPersistenceEntityRepository = orderPersistenceEntityRepository;
    }

    @Test
    public void shouldPersist() {
        OrderPersistenceEntity entity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        orderPersistenceEntityRepository.saveAndFlush(entity);
        Assertions.assertThat(orderPersistenceEntityRepository.existsById(entity.getId())).isTrue();
    }

    @Test
    public void shouldCount() {
        long ordersCount = orderPersistenceEntityRepository.count();
        Assertions.assertThat(ordersCount).isZero();
    }

}