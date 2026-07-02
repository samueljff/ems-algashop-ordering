package com.fonseca.algashop.ordering.core.ports.in.order;

import com.fonseca.algashop.ordering.core.ports.commons.AddressData;
import com.fonseca.algashop.ordering.core.ports.commons.RecipientData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingData {
    private BigDecimal cost;
    private LocalDate expectedDate;
    private RecipientData recipient;
    private AddressData address;
}
