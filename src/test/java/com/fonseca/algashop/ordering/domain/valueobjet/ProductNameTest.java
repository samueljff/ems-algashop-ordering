package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductNameTest {
    @Test
    void givenValidName_whenCreateProductName_thenShouldSucceed() {
        // given
        String name = "Notebook Dell Inspiron";

        // when
        ProductName productName = new ProductName(name);

        // then
        Assertions.assertThat(productName.value()).isEqualTo("Notebook Dell Inspiron");
    }

    @Test
    void givenValidProductName_whenToString_thenShouldReturnValue() {
        // given
        ProductName productName = new ProductName("Mouse Gamer");

        // when
        String result = productName.toString();

        // then
        Assertions.assertThat(result).isEqualTo("Mouse Gamer");
    }

    @Test
    void givenNullValue_whenCreateProductName_thenShouldThrowException() {
        // then
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new ProductName(null));
    }

    @Test
    void givenEmptyValue_whenCreateProductName_thenShouldThrowException() {
        // then
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new ProductName(""));
    }

    @Test
    void givenBlankValue_whenCreateProductName_thenShouldThrowException() {
        // then
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new ProductName("   "));
    }

    @Test
    void givenTwoProductNamesWithSameValue_whenEquals_thenShouldBeEqual() {
        // given
        ProductName productName1 = new ProductName("Teclado Mecânico");
        ProductName productName2 = new ProductName("Teclado Mecânico");

        // then
        Assertions.assertThat(productName1).isEqualTo(productName2);
        Assertions.assertThat(productName1.hashCode()).isEqualTo(productName2.hashCode());
    }

    @Test
    void givenTwoProductNamesWithDifferentValues_whenEquals_thenShouldNotBeEqual() {
        // given
        ProductName productName1 = new ProductName("Teclado Mecânico");
        ProductName productName2 = new ProductName("Mouse Óptico");

        // then
        Assertions.assertThat(productName1).isNotEqualTo(productName2);
    }

    @Test
    void givenNameWithSpecialCharacters_whenCreateProductName_thenShouldSucceed() {
        // given
        String name = "Café Especial - 100% Arábica";

        // when
        ProductName productName = new ProductName(name);

        // then
        Assertions.assertThat(productName.value()).isEqualTo("Café Especial - 100% Arábica");
    }

    @Test
    void givenLongName_whenCreateProductName_thenShouldSucceed() {
        // given
        String longName = "Notebook Dell Inspiron 15 3000 Intel Core i5 8GB RAM 256GB SSD Tela 15.6 Windows 11";

        // when
        ProductName productName = new ProductName(longName);

        // then
        Assertions.assertThat(productName.value()).isEqualTo(longName);
    }
}