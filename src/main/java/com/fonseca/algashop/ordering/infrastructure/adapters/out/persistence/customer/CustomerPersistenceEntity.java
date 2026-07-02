package com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.customer;

import com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.commons.AddressEmbeddable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "\"customer\"")
@Getter
@Setter
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class  CustomerPersistenceEntity extends AbstractAggregateRoot<CustomerPersistenceEntity> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String document;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private Integer loyaltyPoints;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "number", column = @Column(name = "address_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "address_complement")),
            @AttributeOverride(name = "neighborhood", column = @Column(name = "address_neighborhood")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "state", column = @Column(name = "address_state")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "address_zipCode"))
    })
    private AddressEmbeddable address;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Version
    private Long version;

    public Collection<Object> getEvents(){
        return super.domainEvents();
    }

    public void addEvents(Collection<Object> events){
        if (events != null){
            for (Object event : events){
                this.registerEvent(event);
            }
        }
    }
}
