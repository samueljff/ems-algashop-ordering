package com.fonseca.algashop.ordering.infrastructure.persistence.entity;

import com.fonseca.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.fonseca.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = "id")
@Table(name = "\"order\"")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class OrderPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private UUID customerId;

    private BigDecimal totalAmount;
    private Integer totalItems;
    private String status;
    private String paymentMethod;

    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Version
    private Long version;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "billing_first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "billing_last_name")),
            @AttributeOverride(name = "document", column = @Column(name = "billing_document")),
            @AttributeOverride(name = "phone", column = @Column(name = "billing_phone")),
            @AttributeOverride(name = "address.street", column = @Column(name = "billing_address_street")),
            @AttributeOverride(name = "address.number", column = @Column(name = "billing_address_number")),
            @AttributeOverride(name = "address.complement", column = @Column(name = "billing_address_complement")),
            @AttributeOverride(name = "address.neighborhood", column = @Column(name = "billing_address_neighborhood")),
            @AttributeOverride(name = "address.city", column = @Column(name = "billing_address_city")),
            @AttributeOverride(name = "address.state", column = @Column(name = "billing_address_state")),
            @AttributeOverride(name = "address.zipCode", column = @Column(name = "billing_address_zipCode"))
    })
    private BillingEmbeddable billing;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "cost", column = @Column(name = "shipping_cost")),
            @AttributeOverride(name = "expectedDate", column = @Column(name = "shipping_expected_date")),
            @AttributeOverride(name = "recipient.firstName", column = @Column(name = "shipping_recipient_first_name")),
            @AttributeOverride(name = "recipient.lastName", column = @Column(name = "shipping_recipient_last_name")),
            @AttributeOverride(name = "recipient.document", column = @Column(name = "shipping_recipient_document")),
            @AttributeOverride(name = "recipient.phone", column = @Column(name = "shipping_recipient_phone")),
            @AttributeOverride(name = "address.street", column = @Column(name = "shipping_address_street")),
            @AttributeOverride(name = "address.number", column = @Column(name = "shipping_address_number")),
            @AttributeOverride(name = "address.complement", column = @Column(name = "shipping_address_complement")),
            @AttributeOverride(name = "address.neighborhood", column = @Column(name = "shipping_address_neighborhood")),
            @AttributeOverride(name = "address.city", column = @Column(name = "shipping_address_city")),
            @AttributeOverride(name = "address.state", column = @Column(name = "shipping_address_state")),
            @AttributeOverride(name = "address.zipCode", column = @Column(name = "shipping_address_zipCode"))
    })
    private ShippingEmbeddable shipping;
}
