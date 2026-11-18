package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.customer;

import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.core.ports.in.customer.*;
import com.fonseca.algashop.ordering.infrastructure.config.security.SecurityAnnotations;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/api/v1/customers/me")
@RequiredArgsConstructor
public class MyCustomerController {

	private final ForManagingCustomers forManagingCustomers;
	private final ForQueryingCustomers forQueryingCustomers;
	private final SecurityChecks securityChecks;

	@SecurityAnnotations.CanWriteMyCustomerProfile
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CustomerOutput create(@RequestBody @Valid CustomerInput input,
                                 HttpServletResponse httpServletResponse) {
		UUID customerId = forManagingCustomers.create(securityChecks.getAuthenticatedUserId(), input);

		UriComponentsBuilder builder = fromMethodCall(on(MyCustomerController.class).load());
		httpServletResponse.addHeader("Location", builder.toUriString());

		return forQueryingCustomers.findById(customerId);
	}

	@SecurityAnnotations.CanReadMyCustomerProfile
	@GetMapping
	public CustomerOutput load() {
		return forQueryingCustomers.findById(securityChecks.getAuthenticatedUserId());
	}


	@SecurityAnnotations.CanWriteMyCustomerProfile
	@PutMapping
	public CustomerOutput update(@RequestBody @Valid CustomerUpdateInput input) {
		forManagingCustomers.update(securityChecks.getAuthenticatedUserId(), input);
		return forQueryingCustomers.findById(securityChecks.getAuthenticatedUserId());
	}

}