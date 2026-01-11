package com.fonseca.algashop.ordering.domain.model.shoppingcart;

import com.fonseca.algashop.ordering.domain.model.product.ProductOutOfStockException;
import com.fonseca.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public class ShoppingCartTest {

    @Test
    public void givenNewCart_whenStart_shouldBeEmptyWithZeroTotals() {
        // Given
        CustomerId customerId = new CustomerId();

        // When
        ShoppingCart cart = ShoppingCart.startShopping(customerId);

        // Then
        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.id()).isNotNull(),
                c -> Assertions.assertThat(c.customerId()).isEqualTo(customerId),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(Money.ZERO),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(Quantity.ZERO),
                c -> Assertions.assertThat(c.isEmpty()).isTrue(),
                c -> Assertions.assertThat(c.items()).isEmpty(),
                c -> Assertions.assertThat(c.createdAt()).isNotNull()
        );
    }

    @Test
    public void givenCart_whenAddOutOfStockProduct_shouldThrowProductOutOfStockException() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product outOfStockProduct = ProductTestDataBuilder.aProductUnavailable().build();

        // When
        ThrowableAssert.ThrowingCallable addTask = () -> cart.addItem(outOfStockProduct, new Quantity(1));

        // Then
        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(addTask);
    }

    @Test
    public void givenCart_whenAddSameProductTwice_shouldIncrementQuantityAndUpdateProductData() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();

        // When
        cart.addItem(product, new Quantity(2));
        cart.addItem(product, new Quantity(3));

        // Then
        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.items()).hasSize(3),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(new Quantity(8)),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(new Money("21200"))
        );

        ShoppingCartItem item = cart.findItem(product.id());
        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(5)),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("15000"))
        );
    }

    @Test
    public void givenEmptyCart_whenAddNewItem_shouldContainItemAndRecalculateTotals() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();

        // When
        cart.addItem(product, new Quantity(2));

        // Then
        Assertions.assertThat(cart.items()).hasSize(3);
        ShoppingCartItem item = cart.findItem(product.id());
        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.productId()).isEqualTo(product.id()),
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(2))
        );
        Assertions.assertThat(cart.totalItems()).isEqualTo(new Quantity(5));
        Assertions.assertThat(cart.totalAmount()).isEqualTo(
                new Money("12200"));
    }

    @Test
    public void givenCart_whenAddTwoDifferentProducts_shouldAddBothItemsAndCalculateTotals() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product1 = ProductTestDataBuilder.aProduct().build();
        Product product2 = ProductTestDataBuilder.aProductAltMousePad().build();

        // When
        cart.addItem(product1, new Quantity(2));
        cart.addItem(product2, new Quantity(3));

        // Then
        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.items()).hasSize(4),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(new Quantity(8)),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(new Money("12500"))
        );
    }

    @Test
    public void givenCart_whenRemoveNonExistentItem_shouldThrowShoppingCartDoesNotContainItemException() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCartItemId nonExistentId = new ShoppingCartItemId();

        // When
        ThrowableAssert.ThrowingCallable removeTask = () -> cart.removeItem(nonExistentId);

        // Then
        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(removeTask);
    }

    @Test
    public void givenCartWithItems_whenRemoveExistingItem_shouldRemoveAndRecalculateTotals() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(2));

        ShoppingCartItem item = cart.findItem(product.id());
        Assertions.assertThat(cart.totalItems()).isEqualTo(new Quantity(5));
        Assertions.assertThat(cart.totalAmount()).isEqualTo(new Money("12200"));
        // When
        cart.removeItem(item.id());
        // Then
        Assertions.assertThat(cart.totalItems()).isEqualTo(new Quantity(3));
        Assertions.assertThat(cart.totalAmount()).isEqualTo(new Money("6200"));
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
        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.isEmpty()).isTrue(),
                c -> Assertions.assertThat(c.items()).isEmpty(),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(Quantity.ZERO),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(Money.ZERO)
        );
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
        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainProductException.class)
                .isThrownBy(refreshTask);
    }

    @Test
    public void givenCartWithItem_whenRefreshItem_shouldUpdatePriceAndRecalculateTotals() {
        // Given
        Product originalProduct = ProductTestDataBuilder.aProduct().build();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
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
        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(new Money("13200"))
        );

        ShoppingCartItem item = cart.findItem(originalProduct.id());
        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.price()).isEqualTo(new Money("3500")),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("7000"))
        );
    }

    @Test
    public void givenCartWithItem_whenChangeItemQuantity_shouldRecalculateTotals() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(2));

        ShoppingCartItem item = cart.findItem(product.id());

        // When
        cart.changeItemQuantity(item.id(), new Quantity(5));

        // Then
        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(new Quantity(8)),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(new Money("21200"))
        );

        ShoppingCartItem updatedItem = cart.findItem(product.id());
        Assertions.assertWith(updatedItem,
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(5)),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("15000"))
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
        Assertions.assertThatIllegalArgumentException()
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
        Assertions.assertThat(hasUnavailable).isTrue();
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
        Assertions.assertThat(hasUnavailable).isFalse();
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
        Assertions.assertThat(cart1).isEqualTo(cart2);
        Assertions.assertThat(cart1.hashCode()).isEqualTo(cart2.hashCode());
    }

    @Test
    public void givenDifferentIds_whenCompareItems_shouldNotBeEqual() {
        // Given
        ShoppingCart shoppingCart1 = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCart shoppingCart2 = ShoppingCartTestDataBuilder.aShoppingCart().build();

        // When & Then
        Assertions.assertThat(shoppingCart1).isNotEqualTo(shoppingCart2);
    }

    @Test
    public void givenCartWithMultipleItems_whenRemoveOneItem_shouldRecalculateTotalsCorrectly() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
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
        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.items()).hasSize(4),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(new Quantity(6)),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(new Money("12400"))
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
        Assertions.assertThat(isEmpty).isTrue();
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
        Assertions.assertThat(isEmpty).isFalse();
    }

    @Test
    public void givenCart_whenFindItemByProductId_shouldReturnCorrectItem() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().build();
        cart.addItem(product, new Quantity(2));

        // When
        ShoppingCartItem foundItem = cart.findItem(product.id());

        // Then
        Assertions.assertWith(foundItem,
                i -> Assertions.assertThat(i.productId()).isEqualTo(product.id()),
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(2))
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
        Assertions.assertThat(foundItem).isEqualTo(originalItem);
    }

    @Test
    public void givenCart_whenAddItemWithNullProduct_shouldThrowNullPointerException() {
        // Given
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        // When
        ThrowableAssert.ThrowingCallable addTask = () -> cart.addItem(null, new Quantity(1));

        // Then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
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
        Assertions.assertThatExceptionOfType(NullPointerException.class)
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
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(modifyTask);
    }
}
