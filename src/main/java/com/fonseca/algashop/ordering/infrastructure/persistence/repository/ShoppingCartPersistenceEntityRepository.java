package com.fonseca.algashop.ordering.infrastructure.persistence.repository;

import com.fonseca.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartPersistenceEntityRepository extends JpaRepository<ShoppingCartPersistenceEntity, UUID> {
    Optional<ShoppingCartPersistenceEntity> findByCustomer_Id(UUID value);
}
