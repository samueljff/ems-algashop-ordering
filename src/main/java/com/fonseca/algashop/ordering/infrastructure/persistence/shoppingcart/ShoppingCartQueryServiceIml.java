package com.fonseca.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.fonseca.algashop.ordering.core.application.shoppingcart.query.ShoppingCartOutput;
import com.fonseca.algashop.ordering.core.application.shoppingcart.query.ShoppingCartQueryService;
import com.fonseca.algashop.ordering.core.application.utility.Mapper;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartQueryServiceIml implements ShoppingCartQueryService {

    private final ShoppingCartPersistenceEntityRepository persistenceRepository;
    private final Mapper mapper;

    @Override
    public ShoppingCartOutput findById(UUID shoppingCartId) {
        return persistenceRepository.findById(shoppingCartId)
                .map(sc -> mapper.convert(sc, ShoppingCartOutput.class))
                .orElseThrow(() -> new ShoppingCartNotFoundException());
    }

    @Override
    public ShoppingCartOutput findByCustomerId(UUID customerId) {
        return persistenceRepository.findByCustomer_Id(customerId)
                .map(sc -> mapper.convert(sc, ShoppingCartOutput.class))
                .orElseThrow(() -> new ShoppingCartNotFoundException());
    }
}
