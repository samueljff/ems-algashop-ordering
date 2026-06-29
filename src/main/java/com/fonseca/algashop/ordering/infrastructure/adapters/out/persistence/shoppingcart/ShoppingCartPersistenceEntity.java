package com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.fonseca.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "\"shopping_cart\"")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartPersistenceEntity extends AbstractAggregateRoot<ShoppingCartPersistenceEntity> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private BigDecimal totalAmount;
    private Integer totalItems;

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingCartItemPersistenceEntity> items = new HashSet<>();

    @CreatedBy
    private UUID createdByUserId;

    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Version
    private Long version;

    @Builder(toBuilder = true)
    public ShoppingCartPersistenceEntity(UUID id, CustomerPersistenceEntity customer, BigDecimal totalAmount, Integer totalItems, OffsetDateTime createdAt,
                                         Set<ShoppingCartItemPersistenceEntity> items) {
        this.id = id;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.createdAt = createdAt;
        this.replaceItems(items);
    }

    public void addItem(Set<ShoppingCartItemPersistenceEntity> items) {
        for (ShoppingCartItemPersistenceEntity item : items) {
            this.addItem(item);
        }
    }

    public void addItem(ShoppingCartItemPersistenceEntity item) {
        if (item == null) {
            return;
        }
        if (this.getItems() == null) {
            this.setItems(new HashSet<>());
        }
        item.setShoppingCart(this);
        this.items.add(item);
    }

    public UUID getCustomerId() {
        if (customer == null) {
            return null;
        }
        return customer.getId();
    }

    public void replaceItems(Set<ShoppingCartItemPersistenceEntity> updatedItems) {

        if (this.items == null) {
            this.items = new HashSet<>();
        }

        this.items.clear();

        if (updatedItems == null || updatedItems.isEmpty()) {
            return;
        }

        for (ShoppingCartItemPersistenceEntity item : updatedItems) {
            item.setShoppingCart(this);
            this.items.add(item);
        }
    }

    public void addEvents(Collection<Object> events){
        if (events != null){
            for (Object event : events){
                this.registerEvent(event);
            }
        }
    }

}
