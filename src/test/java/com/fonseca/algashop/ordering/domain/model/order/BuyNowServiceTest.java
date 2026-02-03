package com.fonseca.algashop.ordering.domain.model.order;

import com.fonseca.algashop.ordering.domain.model.commons.Money;
import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.fonseca.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.product.ProductOutOfStockException;
import com.fonseca.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class BuyNowServiceTest {


    private BuyNowService buyNowService;

    @Mock
    private Orders orders;

    @BeforeEach
    void setup(){
        CustomerHaveFreeShippingSpecification specification = new CustomerHaveFreeShippingSpecification(
                orders,
                new LoyaltyPoints(100),
                2L,
                new LoyaltyPoints(2000)
        );
        buyNowService = new BuyNowService(specification);
    }

    @Test
    void givenValidProductAndDetails_whenBuyNow_shouldReturnPlacedOrder() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        Quantity quantity = new Quantity(3);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(product, customer, billingInfo, shippingInfo, quantity, paymentMethod);

        assertThat(order).isNotNull();
        assertThat(order.id()).isNotNull();
        assertThat(order.customerId()).isEqualTo(customer.id());
        assertThat(order.billing()).isEqualTo(billingInfo);
        assertThat(order.shipping()).isEqualTo(shippingInfo);
        assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.isPlaced()).isTrue();

        assertThat(order.items()).hasSize(1);
        assertThat(order.items().iterator().next().productId()).isEqualTo(product.id());
        assertThat(order.items().iterator().next().quantity()).isEqualTo(quantity);
        assertThat(order.items().iterator().next().price()).isEqualTo(product.price());

        Money expectedTotalAmount = product.price().multiply(quantity).add(shippingInfo.cost());
        assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
        assertThat(order.totalItems()).isEqualTo(quantity);
    }

    @Test
    void givenOutOfStockProduct_whenBuyNow_shouldThrowProductOutOfStockException() {
        Product product = ProductTestDataBuilder.aProductUnavailable().build();
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        Quantity quantity = new Quantity(1);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> buyNowService.buyNow(product, customer, billingInfo, shippingInfo, quantity, paymentMethod));
    }

    @Test
    void givenInvalidQuantity_whenBuyNow_shouldThrowIllegalArgumentException() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        Quantity quantity = new Quantity(0);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> buyNowService.buyNow(product, customer, billingInfo, shippingInfo, quantity, paymentMethod));
    }

    @Test
    void givenCustomerWithFreeShipping_whenBuyNow_shouldReturnPlacedOrderWithFreeShipping() {
        Mockito.when(orders.salesQuantityByCustomerInYear(
                Mockito.any(CustomerId.class),
                Mockito.any(Year.class)
        )).thenReturn(2L);

        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = CustomerTestDataBuilder.existingCustomer().loyaltyPoints(new LoyaltyPoints(100)).build();
        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        Quantity quantity = new Quantity(3);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(product, customer, billingInfo, shippingInfo, quantity, paymentMethod);

        assertThat(order).isNotNull();
        assertThat(order.id()).isNotNull();
        assertThat(order.customerId()).isEqualTo(customer.id());
        assertThat(order.billing()).isEqualTo(billingInfo);
        assertThat(order.shipping()).isEqualTo(shippingInfo.toBuilder().cost(Money.ZERO).build());
        assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.isPlaced()).isTrue();

        assertThat(order.items()).hasSize(1);
        assertThat(order.items().iterator().next().productId()).isEqualTo(product.id());
        assertThat(order.items().iterator().next().quantity()).isEqualTo(quantity);
        assertThat(order.items().iterator().next().price()).isEqualTo(product.price());

        Money expectedTotalAmount = product.price().multiply(quantity);
        assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
        assertThat(order.totalItems()).isEqualTo(quantity);
    }
}