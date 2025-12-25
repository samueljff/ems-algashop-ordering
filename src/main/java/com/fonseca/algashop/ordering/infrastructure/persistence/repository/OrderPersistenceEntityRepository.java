package com.fonseca.algashop.ordering.infrastructure.persistence.repository;

import com.fonseca.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {
    List<OrderPersistenceEntity> findByCustomer_IdAndPlacedAtBetween(
        UUID customerId,
        OffsetDateTime start,
        OffsetDateTime end
    );
}
