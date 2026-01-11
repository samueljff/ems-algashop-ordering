package com.fonseca.algashop.ordering.domain.model.shoppingcart;

import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.product.ProductName;
import com.fonseca.algashop.ordering.domain.model.product.ProductId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public class ShoppingCartItemTest {

    @Test
    public void givenBrandNewItem_whenCreate_shouldCalculateTotalAmount() {
        // Given
        ShoppingCartId cartId = new ShoppingCartId();
        ProductId productId = new ProductId();
        ProductName productName = new ProductName("Notebook");
        Money price = new Money("1000");
        Quantity quantity = new Quantity(2);

        // When
        ShoppingCartItem item = ShoppingCartItem.brandNew()
                .shoppingCartId(cartId)
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .available(true)
                .build();

        // Then
        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.id()).isNotNull(),
                i -> Assertions.assertThat(i.shoppingCartId()).isEqualTo(cartId),
                i -> Assertions.assertThat(i.productId()).isEqualTo(productId),
                i -> Assertions.assertThat(i.name()).isEqualTo(productName),
                i -> Assertions.assertThat(i.price()).isEqualTo(price),
                i -> Assertions.assertThat(i.quantity()).isEqualTo(quantity),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("2000")),
                i -> Assertions.assertThat(i.isAvailable()).isTrue()
        );
    }

    @Test
    public void givenValidData_whenCreateNewItem_shouldInitializeCorrectly() {
        // Given & When
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .productName(new ProductName("Notebook"))
                .price(new Money("2000"))
                .quantity(new Quantity(2))
                .available(true)
                .build();

        // Then
        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.id()).isNotNull(),
                i -> Assertions.assertThat(i.shoppingCartId()).isNotNull(),
                i -> Assertions.assertThat(i.productId()).isNotNull(),
                i -> Assertions.assertThat(i.name()).isEqualTo(new ProductName("Notebook")),
                i -> Assertions.assertThat(i.price()).isEqualTo(new Money("2000")),
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(2)),
                i -> Assertions.assertThat(i.isAvailable()).isTrue(),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("4000"))
        );
    }

    @Test
    public void givenItem_whenChangeQuantity_shouldRecalculateTotalAmount() {
        // Given
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .price(new Money("500"))
                .quantity(new Quantity(2))
                .build();

        // When
        item.changeQuantity(new Quantity(5));

        // Then
        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(5)),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("2500"))
        );
    }

    @Test
    public void givenItem_whenChangeQuantityToZero_shouldThrowIllegalArgumentException() {
        // Given
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem().build();

        // When
        ThrowableAssert.ThrowingCallable changeTask = () -> item.changeQuantity(Quantity.ZERO);

        // Then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(changeTask);
    }

    @Test
    public void givenItem_whenRefreshWithCompatibleProduct_shouldUpdateProductData() {
        // Given
        ProductId productId = new ProductId();
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .productId(productId)
                .price(new Money("1000"))
                .quantity(new Quantity(2))
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name(new ProductName("Updated Notebook"))
                .price(new Money("1200"))
                .inStock(true)
                .build();

        // When
        item.refresh(updatedProduct);

        // Then
        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.price()).isEqualTo(new Money("1200")),
                i -> Assertions.assertThat(i.name()).isEqualTo(new ProductName("Updated Notebook")),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("2400")),
                i -> Assertions.assertThat(i.isAvailable()).isTrue()
        );
    }

    @Test
    public void givenItem_whenChangePrice_shouldRecalculateTotal() {
        // Given
        ProductId productId = new ProductId();
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .productId(productId)
                .price(new Money("1500"))
                .quantity(new Quantity(2))
                .build();

        Product product = Product.builder()
                .id(productId)
                .name(new ProductName("Notebook"))
                .price(new Money("3000"))
                .inStock(true)
                .build();

        // When
        item.refresh(product);

        // Then
        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.price()).isEqualTo(product.price()),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(product.price().multiply(new Quantity(2)))
        );
    }

    @Test
    public void givenItem_whenChangeAvailability_shouldUpdateStatus() {
        // Given
        ProductId productId = new ProductId();
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .productId(productId)
                .available(true)
                .build();

        Product product = Product.builder()
                .id(productId)
                .name(new ProductName("Notebook"))
                .price(new Money("3000"))
                .inStock(false)
                .build();

        // When
        item.refresh(product);

        // Then
        Assertions.assertThat(item.isAvailable()).isFalse();
    }

    @Test
    public void givenItem_whenRefreshWithIncompatibleProduct_shouldThrowException() {
        // Given
        ProductId originalProductId = new ProductId();
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .productId(originalProductId)
                .build();

        Product differentProduct = Product.builder()
                .id(new ProductId())
                .name(new ProductName("Different Product"))
                .price(new Money("500"))
                .inStock(true)
                .build();

        // When
        ThrowableAssert.ThrowingCallable refreshTask = () -> item.refresh(differentProduct);

        // Then
        Assertions.assertThatExceptionOfType(ShoppingCartItemIncompatibleProductException.class)
                .isThrownBy(refreshTask);
    }

    @Test
    public void givenTwoItemsWithSameId_whenCompare_shouldBeEqual() {
        // Given
        ShoppingCartItemId itemId = new ShoppingCartItemId();
        ShoppingCartId cartId = new ShoppingCartId();
        ProductId productId = new ProductId();

        ShoppingCartItem item1 = ShoppingCartItem.existing()
                .id(itemId)
                .shoppingCartId(cartId)
                .productId(productId)
                .productName(new ProductName("Notebook"))
                .price(new Money("1000"))
                .quantity(new Quantity(2))
                .available(true)
                .totalAmount(new Money("2000"))
                .build();

        ShoppingCartItem item2 = ShoppingCartItem.existing()
                .id(itemId)
                .shoppingCartId(cartId)
                .productId(productId)
                .productName(new ProductName("Notebook"))
                .price(new Money("1500"))
                .quantity(new Quantity(5))
                .available(false)
                .totalAmount(new Money("7500"))
                .build();

        // When & Then
        Assertions.assertThat(item1).isEqualTo(item2);
        Assertions.assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    public void givenEqualIds_whenCompareItems_shouldBeEqual() {
        // Given
        ShoppingCartId cartId = new ShoppingCartId();
        ProductId productId = new ProductId();
        ShoppingCartItemId shoppingCartItemId = new ShoppingCartItemId();

        ShoppingCartItem item1 = ShoppingCartItem.existing()
                .id(shoppingCartItemId)
                .shoppingCartId(cartId)
                .productId(productId)
                .productName(new ProductName("Mouse"))
                .price(new Money("100"))
                .quantity(new Quantity(1))
                .available(true)
                .totalAmount(new Money("100"))
                .build();

        ShoppingCartItem item2 = ShoppingCartItem.existing()
                .id(shoppingCartItemId)
                .shoppingCartId(cartId)
                .productId(productId)
                .productName(new ProductName("Notebook"))
                .price(new Money("100"))
                .quantity(new Quantity(1))
                .available(true)
                .totalAmount(new Money("100"))
                .build();

        // When & Then
        Assertions.assertThat(item1).isEqualTo(item2);
        Assertions.assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    public void givenDifferentIds_whenCompareItems_shouldNotBeEqual() {
        // Given
        ShoppingCartItem item1 = ShoppingCartItemTestDataBuilder.aShoppingCartItem().build();
        ShoppingCartItem item2 = ShoppingCartItemTestDataBuilder.aShoppingCartItem().build();

        // When & Then
        Assertions.assertThat(item1).isNotEqualTo(item2);
    }
}
