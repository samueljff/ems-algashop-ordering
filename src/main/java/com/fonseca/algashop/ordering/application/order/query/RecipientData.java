package com.fonseca.algashop.ordering.application.order.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipientData {
    private String firstName;
    private String lastName;
    private String document;
    private String phone;
}
