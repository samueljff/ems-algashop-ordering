package com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartProductAdjustmentService;
import com.fonseca.algashop.ordering.core.domain.model.commons.Money;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ShoppingCartUpdateProvider implements ShoppingCartProductAdjustmentService {

    private final ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository;

    @Override
    @Transactional
    public void adjustPrice(ProductId productId, Money updatedPrice) {
        shoppingCartPersistenceEntityRepository.updateItemPrice(productId.value(), updatedPrice.value());
        shoppingCartPersistenceEntityRepository.recalculateTotalsForCartsWithProduct(productId.value());
    }

    @Override
    @Transactional
    public void changeAvailability(ProductId productId, boolean available) {
        shoppingCartPersistenceEntityRepository.updateItemAvailability(productId.value(), available);
    }
}
