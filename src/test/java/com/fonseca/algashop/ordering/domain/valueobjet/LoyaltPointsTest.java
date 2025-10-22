package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltPointsTest {

    @Test
    void shouldGenerateWithValue(){
        LoyaltyPoints loyaltPoints = new LoyaltyPoints(10);
        Assertions.assertThat(loyaltPoints.value()).isEqualTo(10);
    }

    @Test
    void givenPositivePoints_whenAdded_thenTotalShouldIncrease(){
        LoyaltyPoints loyaltPoints = new LoyaltyPoints(10);
        Assertions.assertThat(loyaltPoints.add(5).value()).isEqualTo(15);
    }

    @Test
    void givenNegativePoints_whenAdded_thenShouldThrowException(){
        LoyaltyPoints loyaltPoints = new LoyaltyPoints(10);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> loyaltPoints.add(-5));
        Assertions.assertThat(loyaltPoints.value()).isEqualTo(10);
    }
}