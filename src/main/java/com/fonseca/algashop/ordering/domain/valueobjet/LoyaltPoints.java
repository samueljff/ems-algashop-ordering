package com.fonseca.algashop.ordering.domain.valueobjet;

import java.util.Objects;

public record LoyaltPoints(Integer value) implements Comparable<LoyaltPoints> {

    public LoyaltPoints() {
        this(0);
    }

    public LoyaltPoints(Integer value) {
        Objects.requireNonNull(value);
        if (value < 0 ){
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public LoyaltPoints add(Integer value){
        return add(new LoyaltPoints(value));
    }

    public LoyaltPoints add(LoyaltPoints loyaltPoints){

        if (loyaltPoints.value() < 0){
            throw new IllegalArgumentException();
        }

        return new LoyaltPoints(this.value + loyaltPoints.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(LoyaltPoints o) {
        return this.value().compareTo(o.value());
    }
}
