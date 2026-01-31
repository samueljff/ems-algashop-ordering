package com.fonseca.algashop.ordering.application.checkout;

import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.customer.Customers;
import com.fonseca.algashop.ordering.domain.model.order.*;
import com.fonseca.algashop.ordering.domain.model.order.events.OrderPlacedEvent;
import com.fonseca.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.fonseca.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.*;
import com.fonseca.algashop.ordering.infrastructure.listener.order.OrderEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
class CheckoutApplicationServiceIT {

    @Autowired
    private CheckoutApplicationService checkoutApplicationService;

    @Autowired
    private Orders orders;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private OriginAddressService originAddressService;

    @MockitoBean
    private ShippingCostService shippingCostService;

    @MockitoSpyBean
    private OrderEventListener orderEventListener;

    @BeforeEach
    public void setup() {
        Mockito.when(shippingCostService.calculate(Mockito.any(ShippingCostService.CalculationRequest.class)))
                .thenReturn(new ShippingCostService.CalculationResult(
                        new Money("10.00"),
                        LocalDate.now().plusDays(3)
                ));

        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldCreateOrderAndEmptyShoppingCartWhenCheckoutWithValidShoppingCart() {
        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        shoppingCart.addItem(product, new Quantity(2));
        shoppingCarts.add(shoppingCart);

        CheckoutInput checkoutInput = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(shoppingCart.id().value())
                .build();


        String orderIdCreated = checkoutApplicationService.checkout(checkoutInput);

        assertThat(orderIdCreated).isNotNull();
        assertThat(orderIdCreated).isNotBlank();
        assertThat(orders.exists(new OrderId(orderIdCreated))).isTrue();

        Optional<Order> createdOrder = orders.ofId(new OrderId(orderIdCreated));

        assertThat(createdOrder)
                .isPresent()
                .hasValueSatisfying(order -> {
                    assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
                    assertThat(order.totalAmount().value()).isGreaterThan(BigDecimal.ZERO);
                });

        Optional<ShoppingCart> updatedCart = shoppingCarts.ofId(shoppingCart.id());
        assertThat(updatedCart).isPresent();
        assertThat(updatedCart.get().isEmpty()).isTrue();

        Mockito.verify(orderEventListener).listen(Mockito.any(OrderPlacedEvent.class));
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenCheckoutWithNonExistingShoppingCart() {
        CheckoutInput checkoutInput = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(UUID.randomUUID())
                .build();

        assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> checkoutApplicationService.checkout(checkoutInput));
    }

    @Test
    void shouldThrowShoppingCartCantProceedToCheckoutExceptionWhenCheckoutWithEmptyShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        shoppingCarts.add(shoppingCart);

        CheckoutInput checkoutInput = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(shoppingCart.id().value())
                .build();

        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> checkoutApplicationService.checkout(checkoutInput));
    }

    @Test
    void shouldThrowShoppingCartCantProceedToCheckoutExceptionWhenCheckoutWithUnavailableItemsInShoppingCart() {
        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        Product unavailableProduct = ProductTestDataBuilder.aProduct().id(product.id()).inStock(false).build();

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        shoppingCart.addItem(product, new Quantity(1));
        shoppingCart.refreshItem(unavailableProduct);
        shoppingCarts.add(shoppingCart);

        CheckoutInput checkoutInput = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(shoppingCart.id().value())
                .build();

        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> checkoutApplicationService.checkout(checkoutInput));
    }

}