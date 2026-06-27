package com.fonseca.algashop.ordering.application.shoppingcart.query;

import com.fonseca.algashop.ordering.application.AbstractApplicationIT;
import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.customer.Customers;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.fonseca.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class ShoppingCartQueryServiceIT extends AbstractApplicationIT {
    @Autowired
    private ShoppingCartQueryService queryService;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @Test
    void shouldFindShoppingCartById() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());

        Product product1 = ProductTestDataBuilder.aProduct()
                .price(new Money("50.00"))
                .build();
        Product product2 = ProductTestDataBuilder.aProductAltRamMemory()
                .price(new Money("30.00"))
                .build();

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(product2, new Quantity(1));

        shoppingCarts.add(shoppingCart);

        ShoppingCartOutput shoppingCartOutput = queryService.findById(shoppingCart.id().value());

        assertWith(shoppingCartOutput,
                o -> Assertions.assertThat(o.getId()).isEqualTo(shoppingCart.id().value()),
                o -> Assertions.assertThat(o.getCustomerId()).isEqualTo(shoppingCart.customerId().value()),
                o -> assertThat(shoppingCartOutput.getTotalItems()).isEqualTo(3),
                o -> assertThat(shoppingCartOutput.getTotalAmount()).isEqualByComparingTo(new BigDecimal("130.00"))
        );

        assertThat(shoppingCartOutput.getItems())
                .hasSize(2)
                .anySatisfy(item -> {
                    assertThat(item.getQuantity()).isEqualTo(2);
                    assertThat(item.getTotalAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
                })
                .anySatisfy(item -> {
                    assertThat(item.getQuantity()).isEqualTo(1);
                    assertThat(item.getTotalAmount()).isEqualByComparingTo(new BigDecimal("30.00"));
                });
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenCartIdDoesNotExist() {
        ShoppingCartId nonExistentCartId = new ShoppingCartId();

        assertThatThrownBy(() -> queryService.findById(nonExistentCartId.value()))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }

    @Test
    public void shouldFindByCustomerId() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());

        Product product1 = ProductTestDataBuilder.aProduct()
                .price(new Money("50.00"))
                .build();
        Product product2 = ProductTestDataBuilder.aProductAltRamMemory()
                .price(new Money("30.00"))
                .build();

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(product2, new Quantity(1));

        shoppingCarts.add(shoppingCart);

        ShoppingCartOutput shoppingCartOutput = queryService.findByCustomerId(shoppingCart.customerId().value());

        assertWith(shoppingCartOutput,
                o -> Assertions.assertThat(o.getId()).isEqualTo(shoppingCart.id().value()),
                o -> Assertions.assertThat(o.getCustomerId()).isEqualTo(shoppingCart.customerId().value()),
                o -> assertThat(shoppingCartOutput.getTotalItems()).isEqualTo(3),
                o -> assertThat(shoppingCartOutput.getTotalAmount()).isEqualByComparingTo(new BigDecimal("130.00"))
        );

        assertThat(shoppingCartOutput.getItems())
                .hasSize(2)
                .anySatisfy(item -> {
                    assertThat(item.getQuantity()).isEqualTo(2);
                    assertThat(item.getTotalAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
                })
                .anySatisfy(item -> {
                    assertThat(item.getQuantity()).isEqualTo(1);
                    assertThat(item.getTotalAmount()).isEqualByComparingTo(new BigDecimal("30.00"));
                });
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenCustomerIdDoesNotExist() {
        ShoppingCartId nonExistentCustomerId = new ShoppingCartId();

        assertThatThrownBy(() -> queryService.findByCustomerId(nonExistentCustomerId.value()))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }


}