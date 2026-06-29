package com.fonseca.algashop.ordering.core.application.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.core.domain.model.product.Product;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductCatalogService;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductId;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.*;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ForManagingShoppingCarts;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartItemInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService implements ForManagingShoppingCarts {

    private final ShoppingCarts shoppingCarts;
    private final ProductCatalogService productCatalogService;
    private final ShoppingService shoppingService;

    @Transactional
    @Override
    public void addItem(ShoppingCartItemInput input) {
        Objects.requireNonNull(input);
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(input.getShoppingCartId()))
            .orElseThrow(() -> new ShoppingCartNotFoundException());

        ProductId productId = new ProductId(input.getProductId());
        Product product = productCatalogService.ofId(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        shoppingCart.addItem(product, new Quantity(input.getQuantity()));
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    @Override
    public UUID createNew(UUID rawCustomerId) {
        Objects.requireNonNull(rawCustomerId);

        ShoppingCart cart = shoppingService.startShopping(new CustomerId(rawCustomerId));
        shoppingCarts.add(cart);

        return cart.id().value();
    }

    @Transactional
    @Override
    public void removeItem(UUID rawShoppingCartId, UUID rawShoppingCartItemId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
            .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.removeItem(new ShoppingCartItemId(rawShoppingCartItemId));
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    @Override
    public void empty(UUID rawShoppingCartId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
            .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.empty();
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    @Override
    public void delete(UUID rawShoppingCartId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
            .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCarts.remove(shoppingCart.id());
    }
}
