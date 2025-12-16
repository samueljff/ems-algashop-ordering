package com.fonseca.algashop.ordering.infrastructure.persistence.embeddable;

import com.fonseca.algashop.ordering.domain.model.valueObject.Email;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BillingEmbeddable {

    private String firstName;
    private String lastName;
    private String document;
    private String phone;
    private String email;
    @Embedded
    private AddressEmbeddable address;
}
