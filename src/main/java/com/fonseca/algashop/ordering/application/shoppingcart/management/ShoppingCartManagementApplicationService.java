package com.fonseca.algashop.ordering.application.shoppingcart.management;

import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.product.ProductCatalogService;
import com.fonseca.algashop.ordering.domain.model.product.ProductId;
import com.fonseca.algashop.ordering.domain.model.product.ProductNotFoundException;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService {

    private final ShoppingCarts shoppingCarts;
    private final ProductCatalogService productCatalogService;
    private final ShoppingService shoppingService;

    @Transactional
    public void addItem(ShoppingCartItemInput input){
        Objects.requireNonNull(input);
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(input.getShoppingCartId()))
                .orElseThrow(() -> new ShoppingCartNotFoundException());

        Product product = productCatalogService.ofId(new ProductId(input.getProductId()))
                .orElseThrow(ProductNotFoundException::new);

        shoppingCart.addItem(product, new Quantity(input.getQuantity()));
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public UUID createNew(UUID rawCustomerId){
        Objects.requireNonNull(rawCustomerId);

        ShoppingCart cart = shoppingService.startShopping(new CustomerId(rawCustomerId));
        shoppingCarts.add(cart);

        return cart.id().value();
    }

    @Transactional
    public void removeItem(UUID rawShoppingCartId, UUID rawShoppingCartItemId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.removeItem(new ShoppingCartItemId(rawShoppingCartItemId));
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void empty(UUID rawShoppingCartId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.empty();
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void delete(UUID rawShoppingCartId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCarts.remove(shoppingCart.id());
    }
}
