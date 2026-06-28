package com.fonseca.algashop.ordering.core.domain.model.shoppingcart;

import com.fonseca.algashop.ordering.core.domain.model.product.ProductOutOfStockException;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import com.fonseca.algashop.ordering.core.domain.model.commons.Money;
import com.fonseca.algashop.ordering.core.domain.model.product.Product;
import com.fonseca.algashop.ordering.core.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.events.ShoppingCartCreatedEvent;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.events.ShoppingCartItemRemovedEvent;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

public class ShoppingCartTest {

    @Test
    public void givenNewCart_whenStart_shouldBeEmptyWithZeroTotals() {
        // Given
        CustomerId customerId = new CustomerId();

        // When
        ShoppingCart cart = ShoppingCart.startShopping(customerId);

        // Then
        assertWith(cart,
                c -> assertThat(c.id()).isNotNull(),
                c -> assertThat(c.customerId()).isEqualTo(customerId),
                c -> assertThat(c.totalAmount()).isEqualTo(Money.ZERO),
                c -> assertThat(c.totalItems()).isEqualTo(Quantity.ZERO),
                c -> assertThat(c.isEmpty()).isTrue(),
                c -> assertThat(c.items()).isEmpty(),
                c -> assertThat(c.createdAt()).isNotNull()
        );

        ShoppingCartCreatedEvent shoppingCartCreatedEvent = new ShoppingCartCreatedEvent(cart.id(), cart.customerId(), cart.createdAt());

        assertThat(cart.domainEvents()).contains(shoppingCartCreatedEvent);
    }

    @Test
    public void givenCart_whenAddOutOfStockProduct_shouldThrowProductOutOfStockException() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product outOfStockProduct = ProductTestDataBuilder.aProductUnavailable().build();

        // When
        ThrowableAssert.ThrowingCallable addTask = () -> cart.addItem(outOfStockProduct, new Quantity(1));

        // Then
        assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(addTask);
    }

    @Test
    public void givenCart_whenAddSameProductTwice_shouldIncrementQuantityAndUpdateProductData() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();

        // When
        cart.addItem(product, new Quantity(2));
        cart.addItem(product, new Quantity(3));

        // Then
        assertWith(cart,
                c -> assertThat(c.items()).hasSize(1),
                c -> assertThat(c.totalItems()).isEqualTo(new Quantity(5)),
                c -> assertThat(c.totalAmount()).isEqualTo(new Money("15000"))
        );

        ShoppingCartItem item = cart.findItem(product.id());
        assertWith(item,
                i -> assertThat(i.quantity()).isEqualTo(new Quantity(5)),
                i -> assertThat(i.totalAmount()).isEqualTo(new Money("15000"))
        );

        assertThat(cart.domainEvents()).isNotEmpty();
    }

    @Test
    public void givenEmptyCart_whenAddNewItem_shouldContainItemAndRecalculateTotals() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();

        // When
        cart.addItem(product, new Quantity(2));

        // Then
        assertThat(cart.items()).hasSize(1);
        ShoppingCartItem item = cart.findItem(product.id());
        assertWith(item,
                i -> assertThat(i.productId()).isEqualTo(product.id()),
                i -> assertThat(i.quantity()).isEqualTo(new Quantity(2))
        );
        assertThat(cart.totalItems()).isEqualTo(new Quantity(2));
        assertThat(cart.totalAmount()).isEqualTo(
                new Money("6000"));

        assertThat(cart.domainEvents()).isNotEmpty();
    }

    @Test
    public void givenCart_whenAddTwoDifferentProducts_shouldAddBothItemsAndCalculateTotals() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product1 = ProductTestDataBuilder.aProduct().build();
        Product product2 = ProductTestDataBuilder.aProductAltMousePad().build();

        // When
        cart.addItem(product1, new Quantity(2));
        cart.addItem(product2, new Quantity(3));

        // Then
        assertWith(cart,
                c -> assertThat(c.items()).hasSize(2),
                c -> assertThat(c.totalItems()).isEqualTo(new Quantity(5)),
                c -> assertThat(c.totalAmount()).isEqualTo(new Money("6300"))
        );

        assertThat(cart.domainEvents()).isNotEmpty();
    }

    @Test
    public void givenCart_whenRemoveNonExistentItem_shouldThrowShoppingCartDoesNotContainItemException() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCartItemId nonExistentId = new ShoppingCartItemId();

        // When
        ThrowableAssert.ThrowingCallable removeTask = () -> cart.removeItem(nonExistentId);

        // Then
        assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(removeTask);
    }

    @Test
    public void givenCartWithItems_whenRemoveExistingItem_shouldRemoveAndRecalculateTotals() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(2));

        ShoppingCartItem item = cart.findItem(product.id());
        assertThat(cart.totalItems()).isEqualTo(new Quantity(2));
        assertThat(cart.totalAmount()).isEqualTo(new Money("6000"));
        // When
        cart.removeItem(item.id());
        // Then
        assertThat(cart.totalItems()).isEqualTo(new Quantity(0));
        assertThat(cart.totalAmount()).isEqualTo(new Money("0"));

        ShoppingCartItemRemovedEvent shoppingCartItemRemovedEvent = new ShoppingCartItemRemovedEvent(
                cart.id(),
                cart.customerId(),
                product.id(),
                OffsetDateTime.now()
        );

        assertThat(cart.domainEvents()).contains(shoppingCartItemRemovedEvent);
    }

    @Test
    public void givenCartWithItems_whenEmpty_shouldRemoveAllItemsAndResetTotals() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product1 = ProductTestDataBuilder.aProduct().build();
        Product product2 = ProductTestDataBuilder.aProductAltMousePad().build();

        cart.addItem(product1, new Quantity(2));
        cart.addItem(product2, new Quantity(1));

        // When
        cart.empty();

        // Then
        assertWith(cart,
                c -> assertThat(c.isEmpty()).isTrue(),
                c -> assertThat(c.items()).isEmpty(),
                c -> assertThat(c.totalItems()).isEqualTo(Quantity.ZERO),
                c -> assertThat(c.totalAmount()).isEqualTo(Money.ZERO)
        );

        assertThat(cart.domainEvents()).isNotEmpty();
    }

    @Test
    public void givenCartWithItem_whenRefreshWithIncompatibleProduct_shouldThrowShoppingCartDoesNotContainProductException() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product1 = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product1, new Quantity(2));

        Product differentProduct = ProductTestDataBuilder.aProductAltMousePad().build();

        // When
        ThrowableAssert.ThrowingCallable refreshTask = () -> cart.refreshItem(differentProduct);

        // Then
        assertThatExceptionOfType(ShoppingCartDoesNotContainProductException.class)
                .isThrownBy(refreshTask);
    }

    @Test
    public void givenCartWithItem_whenRefreshItem_shouldUpdatePriceAndRecalculateTotals() {
        // Given
        Product originalProduct = ProductTestDataBuilder.aProduct().build();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        cart.addItem(originalProduct, new Quantity(2));

        Product updatedProduct = Product.builder()
                .id(originalProduct.id())
                .name(originalProduct.name())
                .price(new Money("3500"))
                .inStock(true)
                .build();

        // When
        cart.refreshItem(updatedProduct);

        // Then
        assertWith(cart,
                c -> assertThat(c.totalAmount()).isEqualTo(new Money("7000"))
        );

        ShoppingCartItem item = cart.findItem(originalProduct.id());
        assertWith(item,
                i -> assertThat(i.price()).isEqualTo(new Money("3500")),
                i -> assertThat(i.totalAmount()).isEqualTo(new Money("7000"))
        );
    }

    @Test
    public void givenCartWithItem_whenChangeItemQuantity_shouldRecalculateTotals() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(2));

        ShoppingCartItem item = cart.findItem(product.id());

        // When
        cart.changeItemQuantity(item.id(), new Quantity(4));

        // Then
        assertWith(cart,
                c -> assertThat(c.totalItems()).isEqualTo(new Quantity(4)),
                c -> assertThat(c.totalAmount()).isEqualTo(new Money("12000"))
        );

        ShoppingCartItem updatedItem = cart.findItem(product.id());
        assertWith(updatedItem,
                i -> assertThat(i.quantity()).isEqualTo(new Quantity(4)),
                i -> assertThat(i.totalAmount()).isEqualTo(new Money("12000"))
        );
    }

    @Test
    public void givenCartWithItems_whenChangeQuantityToZero_shouldThrowIllegalArgumentException() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(1));

        ShoppingCartItem item = cart.findItem(product.id());

        // When & Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> cart.changeItemQuantity(item.id(), Quantity.ZERO));
    }

    @Test
    public void givenCartWithUnavailableItem_whenCheckAvailability_shouldReturnTrue() {
        // Given
        Product availableProduct = ProductTestDataBuilder.aProduct().build();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        cart.addItem(availableProduct, new Quantity(1));

        Product unavailableProduct = Product.builder()
                .id(availableProduct.id())
                .name(availableProduct.name())
                .price(availableProduct.price())
                .inStock(false)
                .build();

        cart.refreshItem(unavailableProduct);

        // When
        boolean hasUnavailable = cart.containsUnavailableItems();

        // Then
        assertThat(hasUnavailable).isTrue();
    }

    @Test
    public void givenCartWithOnlyAvailableItems_whenCheckAvailability_shouldReturnFalse() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product1 = ProductTestDataBuilder.aProduct().build();
        Product product2 = ProductTestDataBuilder.aProductAltMousePad().build();

        cart.addItem(product1, new Quantity(1));
        cart.addItem(product2, new Quantity(1));

        // When
        boolean hasUnavailable = cart.containsUnavailableItems();

        // Then
        assertThat(hasUnavailable).isFalse();
    }

    @Test
    public void givenTwoCartsWithSameId_whenCompare_shouldBeEqual() {
        // Given
        CustomerId customerId = new CustomerId();
        ShoppingCart cart1 = ShoppingCart.startShopping(customerId);
        ShoppingCart cart2 = ShoppingCart.startShopping(customerId);

        try {
            java.lang.reflect.Field idField = ShoppingCart.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(cart2, cart1.id());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // When & Then
        assertThat(cart1).isEqualTo(cart2);
        assertThat(cart1.hashCode()).isEqualTo(cart2.hashCode());
    }

    @Test
    public void givenDifferentIds_whenCompareItems_shouldNotBeEqual() {
        // Given
        ShoppingCart shoppingCart1 = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCart shoppingCart2 = ShoppingCartTestDataBuilder.aShoppingCart().build();

        // When & Then
        assertThat(shoppingCart1).isNotEqualTo(shoppingCart2);
    }

    @Test
    public void givenCartWithMultipleItems_whenRemoveOneItem_shouldRecalculateTotalsCorrectly() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product1 = ProductTestDataBuilder.aProduct().build();
        Product product2 = ProductTestDataBuilder.aProductAltMousePad().build();
        Product product3 = ProductTestDataBuilder.aProductAltRamMemory().build();

        cart.addItem(product1, new Quantity(2));
        cart.addItem(product2, new Quantity(3));
        cart.addItem(product3, new Quantity(1));

        ShoppingCartItem itemToRemove = cart.findItem(product2.id());

        // When
        cart.removeItem(itemToRemove.id());

        // Then
        assertWith(cart,
                c -> assertThat(c.items()).hasSize(2),
                c -> assertThat(c.totalItems()).isEqualTo(new Quantity(3)),
                c -> assertThat(c.totalAmount()).isEqualTo(new Money("6200"))
        );
    }

    @Test
    public void givenEmptyCart_whenCheckIfEmpty_shouldReturnTrue() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        // When
        cart.empty();
        boolean isEmpty = cart.isEmpty();

        // Then
        assertThat(isEmpty).isTrue();
    }

    @Test
    public void givenCartWithItems_whenCheckIfEmpty_shouldReturnFalse() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(1));

        // When
        boolean isEmpty = cart.isEmpty();

        // Then
        assertThat(isEmpty).isFalse();
    }

    @Test
    public void givenCart_whenFindItemByProductId_shouldReturnCorrectItem() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(2));

        // When
        ShoppingCartItem foundItem = cart.findItem(product.id());

        // Then
        assertWith(foundItem,
                i -> assertThat(i.productId()).isEqualTo(product.id()),
                i -> assertThat(i.quantity()).isEqualTo(new Quantity(2))
        );
    }

    @Test
    public void givenCart_whenFindItemByItemId_shouldReturnCorrectItem() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(2));

        ShoppingCartItem originalItem = cart.findItem(product.id());

        // When
        ShoppingCartItem foundItem = cart.findItem(originalItem.id());

        // Then
        assertThat(foundItem).isEqualTo(originalItem);
    }

    @Test
    public void givenCart_whenAddItemWithNullProduct_shouldThrowNullPointerException() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        // When
        ThrowableAssert.ThrowingCallable addTask = () -> cart.addItem(null, new Quantity(1));

        // Then
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(addTask);
    }

    @Test
    public void givenCart_whenAddItemWithNullQuantity_shouldThrowNullPointerException() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();

        // When
        ThrowableAssert.ThrowingCallable addTask = () -> cart.addItem(product, null);

        // Then
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(addTask);
    }

    @Test
    public void givenCart_whenItemsCollectionIsReturned_shouldBeUnmodifiable() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(1));

        // When
        ThrowableAssert.ThrowingCallable modifyTask = () ->
                cart.items().add(ShoppingCartItemTestDataBuilder.aShoppingCartItem().build());

        // Then
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(modifyTask);
    }
}
