package com.fonseca.algashop.ordering.presetation;

import com.fonseca.algashop.ordering.application.customer.management.CustomerInput;
import com.fonseca.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.fonseca.algashop.ordering.application.customer.query.CustomerFilter;
import com.fonseca.algashop.ordering.application.customer.query.CustomerOutput;
import com.fonseca.algashop.ordering.application.customer.query.CustomerQueryService;
import com.fonseca.algashop.ordering.application.customer.query.CustomerSummaryOutput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerManagementApplicationService customerManagementApplicationService;
    private final CustomerQueryService customerQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOutput create(@RequestBody @Valid CustomerInput input){
        UUID customerId = customerManagementApplicationService.create(input);
        return customerQueryService.findById(customerId);
    }

    @GetMapping
    public PageModel<CustomerSummaryOutput> findAll(CustomerFilter customerFilter) {
        return PageModel.of(customerQueryService.filter(customerFilter));
    }
}
