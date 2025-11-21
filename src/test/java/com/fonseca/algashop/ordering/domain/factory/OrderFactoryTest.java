package com.fonseca.algashop.ordering.domain.factory;

import com.fonseca.algashop.ordering.domain.entity.Order;
import com.fonseca.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.fonseca.algashop.ordering.domain.entity.PaymentMethod;
import com.fonseca.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.fonseca.algashop.ordering.domain.exceptions.OrderCannotBeEditedException;
import com.fonseca.algashop.ordering.domain.valueObject.Billing;
import com.fonseca.algashop.ordering.domain.valueObject.Product;
import com.fonseca.algashop.ordering.domain.valueObject.Quantity;
import com.fonseca.algashop.ordering.domain.valueObject.Shipping;
import com.fonseca.algashop.ordering.domain.valueObject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;


class OrderFactoryTest {

    @Test
    public void shouldGenerateFilledOrderThatCanBePlaced() {
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Billing billing = OrderTestDataBuilder.aBilling();

        Product product = ProductTestDataBuilder.aProduct().build();
        PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

        Quantity quantity = new Quantity(1);
        CustomerId customerId = new CustomerId();

        Order order = OrderFactory.filled(
                customerId, shipping, billing, paymentMethod, product, quantity
        );

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.shipping()).isEqualTo(shipping),
                o -> Assertions.assertThat(o.billing()).isEqualTo(billing),
                o -> Assertions.assertThat(o.paymentMethod()).isEqualTo(paymentMethod),
                o -> Assertions.assertThat(o.items()).isNotEmpty(),
                o -> Assertions.assertThat(o.customerId()).isNotNull(),
                o -> Assertions.assertThat(o.isDraft()).isTrue()
        );

        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();

        // ========== Teste da nova regra de Imutabilidade ==========

        // Assert - Tentar adicionar item e verificar que lança exceção
        Product newProduct = ProductTestDataBuilder.aProductAltMousePad().build();
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.addItem(newProduct, new Quantity(1)));

        // Assert - Tentar alterar billing e verificar que lança exceção
        Billing newBilling = OrderTestDataBuilder.aBilling();
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeBilling(newBilling));

        // Assert - Tentar alterar shipping e verificar que lança exceção
        Shipping newShipping = OrderTestDataBuilder.aShipping();
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeShipping(newShipping));

        // Assert - Tentar alterar payment method e verificar que lança exceção
        ThrowableAssert.ThrowingCallable changePaymentTask = () -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(changePaymentTask);
    }
}