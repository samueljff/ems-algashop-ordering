package com.fonseca.algashop.ordering.infrastructure.persistence.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ShippingEmbeddable {

    private BigDecimal cost;
    private LocalDate expectedDate;
    @Embedded
    private AddressEmbeddable address;
    @Embedded
    private RecipientEmbeddable recipient;
}
