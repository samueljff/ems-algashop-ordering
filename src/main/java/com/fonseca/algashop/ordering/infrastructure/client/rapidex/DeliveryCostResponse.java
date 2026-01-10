package com.fonseca.algashop.ordering.infrastructure.client.rapidex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCostResponse {

    private String deliveryCost;
    private Long estimatedDaysToDeliver;
}
