package com.fonseca.algashop.ordering.infrastructure.fake;

import com.fonseca.algashop.ordering.domain.model.service.ShippingCostService;
import com.fonseca.algashop.ordering.domain.model.valueObject.Money;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ShippingCostServiceImpl implements ShippingCostService {

    @Override
    public CalculationResult calculate(CalculationRequest request) {
        return new CalculationResult(
                new Money("20"),
                LocalDate.now().plusDays(5)
        );
    }
}
