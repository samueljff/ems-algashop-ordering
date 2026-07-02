package com.fonseca.algashop.ordering.core.ports.in.checkout;

import com.fonseca.algashop.ordering.core.ports.commons.AddressData;
import com.fonseca.algashop.ordering.core.ports.commons.RecipientData;
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
