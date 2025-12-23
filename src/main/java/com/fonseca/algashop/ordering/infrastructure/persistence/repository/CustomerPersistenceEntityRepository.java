package com.fonseca.algashop.ordering.infrastructure.persistence.repository;

import com.fonseca.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerPersistenceEntityRepository extends JpaRepository<CustomerPersistenceEntity, UUID> {
}
