package com.fonseca.algashop.ordering.infrastructure.persistence.repository;

import com.fonseca.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {

    @Query("""
            SELECT o
            FROM OrderPersistenceEntity o
                WHERE o.customer.id = :customerId
                AND YEAR(o.placedAt) = :year
    """)
    List<OrderPersistenceEntity> placedByCustomerInYear(
            @Param("customerId") UUID customerId,
            @Param("year") Integer year
    );
}
