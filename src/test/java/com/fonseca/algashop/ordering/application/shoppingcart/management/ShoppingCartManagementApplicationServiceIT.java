package com.fonseca.algashop.ordering.application.shoppingcart.management;

import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.customer.*;
import com.fonseca.algashop.ordering.domain.model.product.*;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.*;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.events.ShoppingCartCreatedEvent;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.events.ShoppingCartEmptiedEvent;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.events.ShoppingCartItemAddedEvent;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.events.ShoppingCartItemRemovedEvent;
import com.fonseca.algashop.ordering.infrastructure.listener.shoppingcart.ShoppingCartEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class ShoppingCartManagementApplicationServiceIT {

    @Autowired
    private ShoppingCartManagementApplicationService service;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @MockitoBean
    private ProductCatalogService productCatalogService;

    @MockitoSpyBean
    private ShoppingCartEventListener shoppingCartEventListener;

    @Test
    void shouldAddItemToShoppingCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Product product = ProductTestDataBuilder.aProduct().build();

        Mockito.when(productCatalogService.ofId(product.id()))
                .thenReturn(Optional.of(product));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value())
                .productId(product.id().value())
                .quantity(3)
                .build();

        service.addItem(input);

        Mockito.verify(shoppingCartEventListener).listen(Mockito.any(ShoppingCartItemAddedEvent.class));

        ShoppingCart updatedShoppingCart = shoppingCarts.ofId(shoppingCart.id()).orElseThrow();

        Assertions.assertThat(updatedShoppingCart.items())
                .hasSize(1)
                .singleElement()
                .satisfies(item -> {
                    Assertions.assertThat(item.productId()).isEqualTo(product.id());
                    Assertions.assertThat(item.quantity().value()).isEqualTo(3);
                });
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenShoppingCartIdDoesNotExist() {
        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .quantity(1)
                .build();

        Assertions.assertThatThrownBy(() -> service.addItem(input))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductIdDoesNotExist() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Mockito.when(productCatalogService.ofId(Mockito.any()))
                .thenReturn(Optional.empty());

        ShoppingCartItemInput itemInput = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value())
                .productId(UUID.randomUUID())
                .quantity(1)
                .build();

        Assertions.assertThatThrownBy(() -> service.addItem(itemInput))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void shouldThrowProductOutOfStockExceptionWhenAddingOutOfStockProduct() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Product outOfStockProduct = ProductTestDataBuilder.aProduct().inStock(false).build();
        Mockito.when(productCatalogService.ofId(outOfStockProduct.id())).thenReturn(Optional.of(outOfStockProduct));

        ShoppingCartItemInput shoppingCartItemInput = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value())
                .productId(outOfStockProduct.id().value())
                .quantity(1)
                .build();

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> service.addItem(shoppingCartItemInput));
    }

    @Test
    void shouldCreateNewCartWhenCustomerHasNoExistingCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        UUID cartId = service.createNew(customer.id().value());

        Mockito.verify(shoppingCartEventListener).listen(Mockito.any(ShoppingCartCreatedEvent.class));

        Assertions.assertThat(shoppingCarts.ofId(new ShoppingCartId(cartId)))
                .isPresent();
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenCustomerIdDoesNotExist() {
        UUID nonExistingCustomerId = UUID.randomUUID();
        Assertions.assertThatThrownBy(() ->
                service.createNew(nonExistingCustomerId)
        ).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void shouldThrowCustomerAlreadyHaveShoppingCartExceptionForCustomerWithExistingCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Assertions.assertThatThrownBy(() ->
                service.createNew(customer.id().value())
        ).isInstanceOf(CustomerAlreadyHaveShoppingCartException.class);
    }

    @Test
    void shouldRemoveItemFromShoppingCartWhenItemExists() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());

        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        shoppingCart.addItem(product, new Quantity(1));
        shoppingCarts.add(shoppingCart);

        ShoppingCartItem item = shoppingCart.items().iterator().next();

        service.removeItem(shoppingCart.id().value(), item.id().value());

        Mockito.verify(shoppingCartEventListener).listen(Mockito.any(ShoppingCartItemRemovedEvent.class));

        ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id()).orElseThrow();
        Assertions.assertThat(updatedCart.items()).isEmpty();
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenRemovingItemFromNonExistingShoppingCart() {
        UUID nonExistingCartId = UUID.randomUUID();
        UUID nonExistingShoppingCartItemId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> service.removeItem(nonExistingCartId, nonExistingShoppingCartItemId));
    }

    @Test
    void shouldThrowShoppingCartDoesNotContainItemExceptionWhenRemovingNonExistingItem() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        UUID nonExistingItemId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(() -> service.removeItem(shoppingCart.id().value(), nonExistingItemId));
    }

    @Test
    void shouldEmptyShoppingCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        shoppingCart.addItem(product, new Quantity(1));
        shoppingCarts.add(shoppingCart);

        service.empty(shoppingCart.id().value());

        Mockito.verify(shoppingCartEventListener).listen(Mockito.any(ShoppingCartEmptiedEvent.class));

        ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id()).orElseThrow();
        Assertions.assertThat(updatedCart.isEmpty()).isTrue();
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenEmptyingNonExistingShoppingCart() {
        UUID nonExistingCartId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> service.empty(nonExistingCartId));
    }

    @Test
    void shouldDeleteShoppingCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        service.delete(shoppingCart.id().value());

        Optional<ShoppingCart> cartExcluded = shoppingCarts.ofId(shoppingCart.id());
        Assertions.assertThat(cartExcluded).isNotPresent();
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenDeletingNonExistingShoppingCart() {
        UUID nonExistingCartId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> service.delete(nonExistingCartId));
    }
}