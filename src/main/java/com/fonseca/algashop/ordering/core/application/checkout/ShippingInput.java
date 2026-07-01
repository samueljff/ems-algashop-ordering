package com.fonseca.algashop.ordering.core.application.checkout;

import com.fonseca.algashop.ordering.core.ports.commons.AddressData;
import com.fonseca.algashop.ordering.core.application.order.query.RecipientData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingInput {
    @Valid
    @NotNull
    private RecipientData recipient;
    @Valid
    @NotNull
    private AddressData address;
}
