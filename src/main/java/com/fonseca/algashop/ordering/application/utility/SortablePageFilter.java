package com.fonseca.algashop.ordering.application.utility;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SortablePageFilter<T> extends PageFilter {
    private T sortByProperty;
    private Sort.Direction sortDirection;

    public abstract T getSortByPropertyOrDefault();
    public abstract Sort.Direction getSortDirectionOrDefault();
}
