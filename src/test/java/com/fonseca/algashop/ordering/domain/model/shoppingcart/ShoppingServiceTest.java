package com.fonseca.algashop.ordering.domain.model.shoppingcart;

import com.fonseca.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.fonseca.algashop.ordering.domain.model.customer.Customers;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceTest {

    @Mock
    private ShoppingCarts shoppingCarts;

    @Mock
    private Customers customers;

    @InjectMocks
    private ShoppingService shoppingService;

    @Test
    void givenExistingCustomerWithoutShoppingCart_whenStartShopping_thenCreateNewCart() {
        // given
        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        when(customers.exists(customerId)).thenReturn(true);
        when(shoppingCarts.ofCustomer(customerId)).thenReturn(Optional.empty());

        // when
        ShoppingCart shoppingCart = shoppingService.startShopping(customerId);

        // then
        assertThat(shoppingCart).isNotNull();
        assertThat(shoppingCart.customerId()).isEqualTo(customerId);
        assertThat(shoppingCart.isEmpty()).isTrue();

        verify(customers).exists(customerId);
        verify(shoppingCarts).ofCustomer(customerId);
    }

    @Test
    void givenNonExistingCustomer_whenStartShopping_thenThrowCustomerNotFoundException() {
        // given
        CustomerId customerId = new CustomerId();

        when(customers.exists(customerId)).thenReturn(false);

        // when / then
        assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> shoppingService.startShopping(customerId));

        verify(customers).exists(customerId);
        verify(shoppingCarts, never()).ofCustomer(any());
    }

    @Test
    void givenCustomerWithExistingShoppingCart_whenStartShopping_thenShouldCustomerAlreadyHaveShoppingCartException() {
        // given
        CustomerId customerId = new CustomerId();
        ShoppingCart existingCart = ShoppingCart.startShopping(customerId);

        when(customers.exists(customerId)).thenReturn(true);
        when(shoppingCarts.ofCustomer(customerId)).thenReturn(Optional.of(existingCart));

        // when / then
        assertThatExceptionOfType(CustomerAlreadyHaveShoppingCartException.class)
                .isThrownBy(() -> shoppingService.startShopping(customerId));

        verify(customers).exists(customerId);
        verify(shoppingCarts).ofCustomer(customerId);
    }

}