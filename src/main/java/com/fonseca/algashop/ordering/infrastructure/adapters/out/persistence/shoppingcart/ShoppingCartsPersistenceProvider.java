package com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCarts;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartsPersistenceProvider implements ShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository;
    private final ShoppingCartPersistenceEntityAssembler assembler;
    private final ShoppingCartPersistenceEntityDisassembler disassembler;

    private final EntityManager entityManager;

    @Override
    public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
        return shoppingCartPersistenceEntityRepository.findById(shoppingCartId.value())
                .map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(ShoppingCartId shoppingCartId) {
        return shoppingCartPersistenceEntityRepository.existsById(shoppingCartId.value());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(ShoppingCart aggregateRoot) {
        UUID ShoppingCartId = aggregateRoot.id().value();

        shoppingCartPersistenceEntityRepository.findById(ShoppingCartId)
                .ifPresentOrElse(
                        (persistenceEntity) -> update(persistenceEntity, aggregateRoot),
                        ()-> insert(aggregateRoot)
                );
    }

    @Override
    public long count() {
        return shoppingCartPersistenceEntityRepository.count();
    }

    private void update(ShoppingCartPersistenceEntity persistenceEntity, ShoppingCart aggregateRoot) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntity = shoppingCartPersistenceEntityRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    private void insert(ShoppingCart aggregateRoot) {
        ShoppingCartPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        shoppingCartPersistenceEntityRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity persistenceEntity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(ShoppingCart shoppingCart) {
        this.shoppingCartPersistenceEntityRepository.deleteById(shoppingCart.id().value());
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(ShoppingCartId shoppingCartId) {
        this.shoppingCartPersistenceEntityRepository.deleteById(shoppingCartId.value());
    }

    @Override
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        return shoppingCartPersistenceEntityRepository.findByCustomer_Id(customerId.value())
                .map(disassembler::toDomainEntity);
    }
}
