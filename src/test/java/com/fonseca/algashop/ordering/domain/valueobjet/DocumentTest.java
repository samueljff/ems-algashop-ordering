package com.fonseca.algashop.ordering.domain.valueobjet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {
    @Test
    void givenValidDocument_whenCreate_thenShouldSucceed() {
        // given
        String validDoc = "12345678900";

        // when
        Document document = new Document(validDoc);

        // then
        Assertions.assertThat(document.value()).isEqualTo(validDoc);
        Assertions.assertThat(document.toString()).isEqualTo(validDoc);
    }

    @Test
    void givenNullDocument_whenCreate_thenShouldThrowNullPointerException() {
        // when / then
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Document(null));
    }

    @Test
    void givenBlankDocument_whenCreate_thenShouldThrowIllegalArgumentException() {
        // when / then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Document(" "));
    }

    @Test
    void givenEmptyDocument_whenCreate_thenShouldThrowIllegalArgumentException() {
        // when / then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Document(""));
    }

    @Test
    void givenValidDocument_whenToString_thenShouldReturnValue() {
        // given
        String validDoc = "98765432100";
        Document document = new Document(validDoc);

        // when
        String result = document.toString();

        // then
        Assertions.assertThat(result).isEqualTo(validDoc);
    }
}