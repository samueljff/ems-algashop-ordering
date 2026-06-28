package com.fonseca.algashop.ordering.core.application.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageFilter {
    private int size = 15;
    private int page = 0;
}
