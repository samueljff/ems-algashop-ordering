package com.fonseca.algashop.ordering.infrastructure.persistence.disassembler;

import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.fonseca.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CustomerPersistenceEntityDisassemblerTest {

    private final CustomerPersistenceEntityDisassembler disassembler = new CustomerPersistenceEntityDisassembler();

    @Test
    public void shouldConvertToDomain() {
        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();
        Customer domainEntity = disassembler.toDomainEntity(persistenceEntity);
        assertThat(domainEntity).satisfies(
                c -> assertThat(c.id()).isEqualTo(new CustomerId(persistenceEntity.getId())),
                c -> assertThat(c.fullName().firstName()).isEqualTo(persistenceEntity.getFirstName()),
                c -> assertThat(c.fullName().lastName()).isEqualTo(persistenceEntity.getLastName()),
                c -> assertThat(c.birthDate().value()).isEqualTo(persistenceEntity.getBirthDate()),
                c -> assertThat(c.email().value()).isEqualTo(persistenceEntity.getEmail()),
                c -> assertThat(c.phone().value()).isEqualTo(persistenceEntity.getPhone()),
                c -> assertThat(c.document().value()).isEqualTo(persistenceEntity.getDocument()),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isEqualTo(persistenceEntity.getPromotionNotificationsAllowed()),
                c -> assertThat(c.isArchived()).isEqualTo(persistenceEntity.getArchived()),
                c -> assertThat(c.registeredAt()).isEqualTo(persistenceEntity.getRegisteredAt()),
                c -> assertThat(c.archivedAt()).isEqualTo(persistenceEntity.getArchivedAt()),
                c -> assertThat(c.loyaltyPoints().value()).isEqualTo(persistenceEntity.getLoyaltyPoints()),
                c -> assertThat(c.address().city()).isEqualTo(persistenceEntity.getAddress().getCity()),
                c -> assertThat(c.address().neighborhood()).isEqualTo(persistenceEntity.getAddress().getNeighborhood()),
                c -> assertThat(c.address().number()).isEqualTo(persistenceEntity.getAddress().getNumber()),
                c -> assertThat(c.address().street()).isEqualTo(persistenceEntity.getAddress().getStreet()),
                c -> assertThat(c.address().state()).isEqualTo(persistenceEntity.getAddress().getState()),
                c -> assertThat(c.address().zipCode().value()).isEqualTo(persistenceEntity.getAddress().getZipCode()),
                c -> assertThat(c.address().complement()).isEqualTo(persistenceEntity.getAddress().getComplement()),
                c -> assertThat(c.version()).isEqualTo(persistenceEntity.getVersion())

        );
    }
}
