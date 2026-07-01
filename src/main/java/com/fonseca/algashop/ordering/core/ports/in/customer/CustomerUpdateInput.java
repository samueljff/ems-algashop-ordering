package com.fonseca.algashop.ordering.core.ports.in.customer;

import com.fonseca.algashop.ordering.core.ports.commons.AddressData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerUpdateInput {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String phone;
    @NotNull
    private Boolean promotionNotificationsAllowed;
    @NotNull
    @Valid
    private AddressData address;
}
