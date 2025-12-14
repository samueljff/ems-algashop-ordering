package com.fonseca.algashop.ordering.infrastructure.persistence.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressEmbeddable {

    private String street;
    private String complement;
    private String neighborhood;
    private String number;
    private String city;
    private String state;
    private String zipCode;
}
