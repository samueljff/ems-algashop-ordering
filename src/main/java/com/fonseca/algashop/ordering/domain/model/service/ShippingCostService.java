package com.fonseca.algashop.ordering.domain.model.service;

import com.fonseca.algashop.ordering.domain.model.valueObject.Money;
import com.fonseca.algashop.ordering.domain.model.valueObject.ZipCode;
import lombok.Builder;

import java.time.LocalDate;

public interface ShippingCostService {

    CalculationResult calculate(CalculationRequest request);

    @Builder
    record CalculationRequest(ZipCode origin, ZipCode destination){}

    @Builder
    record CalculationResult(Money cost, LocalDate expectedDate){}
}
