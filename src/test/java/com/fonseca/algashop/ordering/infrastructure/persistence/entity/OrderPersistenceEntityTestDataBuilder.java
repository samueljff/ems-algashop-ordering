package com.fonseca.algashop.ordering.infrastructure.persistence.entity;

import com.fonseca.algashop.ordering.domain.model.utility.IdGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static com.fonseca.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity.*;

public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntityBuilder existingOrder() {
        return OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customerId(IdGenerator.generateTimeBasedUUID())
                .totalItems(2)
                .totalAmount(new BigDecimal(1000))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now());
    }
}
