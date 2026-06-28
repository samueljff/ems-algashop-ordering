package com.fonseca.algashop.ordering.core.application.order.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerMinimalOutput {
    private UUID id;
    private String lastName;
    private String firstName;
    private String email;
    private String document;
    private String phone;
}
