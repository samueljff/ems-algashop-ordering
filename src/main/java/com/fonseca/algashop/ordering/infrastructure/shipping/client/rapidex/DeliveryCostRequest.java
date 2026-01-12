package com.fonseca.algashop.ordering.infrastructure.shipping.client.rapidex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCostRequest {
    private String originZipCode;
    private String destinationZipCode;
}
