package com.fonseca.algashop.ordering.domain.valueobjet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money(BigDecimal value) {
        Objects.requireNonNull(value);
        this.value = value.setScale(2, roundingMode);
        if (this.value.signum() == -1) {
            throw new IllegalArgumentException("O valor monetário não pode ser negativo");
        }
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }
        BigDecimal result = this.value.multiply(new BigDecimal(quantity.value()));
        return new Money(result);
    }

    public Money add(Money money) {
        Objects.requireNonNull(money);
        return new Money(this.value.add(money.value));
    }

    public Money divide(Money divisor) {
        BigDecimal result = this.value.divide(divisor.value, 2, roundingMode);
        return new Money(result);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(Money o) {
        return this.value.compareTo(o.value);
    }
}
