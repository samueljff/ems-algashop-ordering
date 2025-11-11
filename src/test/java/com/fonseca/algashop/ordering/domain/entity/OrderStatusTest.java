package com.fonseca.algashop.ordering.domain.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    public void canChangeTo(){
        Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED)).isTrue();
        Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.CANCELED)).isTrue();
        Assertions.assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.DRAFT)).isFalse();
    }

    @Test
    public void canNotChangeTo(){
        Assertions.assertThat(OrderStatus.PLACED.canNotChangeTo(OrderStatus.DRAFT)).isTrue();
    }
}