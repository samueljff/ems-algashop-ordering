package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.shoppingcart;

import com.fonseca.algashop.ordering.core.application.security.SecurityChecks;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ForManagingShoppingCarts;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartItemInput;
import com.fonseca.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.UnprocessableEntityException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import static com.fonseca.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanReadMyShoppingCart;
import static com.fonseca.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanWriteMyShoppingCart;

@RestController
@RequestMapping("/api/v1/customers/me/shopping-cart")
@RequiredArgsConstructor
public class MyShoppingCartController {

	private final ForManagingShoppingCarts forManagingShoppingCarts;
	private final ForQueryingShoppingCarts forQueryingShoppingCarts;
	private final SecurityChecks securityChecks;

	@CanWriteMyShoppingCart
	@PostMapping
	public ResponseEntity<ShoppingCartOutput> create() {
		forManagingShoppingCarts.createNew(securityChecks.getAuthenticatedUserId());
		return ResponseEntity.created(URI.create("/api/v1/customers/me/shopping-cart"))
				.body(loadAuthenticatedCustomerShoppingCart());
	}

	@CanReadMyShoppingCart
	@GetMapping
	public ShoppingCartOutput findById() {
		return loadAuthenticatedCustomerShoppingCart();
	}

	@CanReadMyShoppingCart
	@GetMapping("/items")
	public ShoppingCartItemListModel findCartItems() {
		return new ShoppingCartItemListModel(loadAuthenticatedCustomerShoppingCart().getItems());
	}

	@CanWriteMyShoppingCart
	@DeleteMapping("/items")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void empty() {
		ShoppingCartOutput shoppingCart = loadAuthenticatedCustomerShoppingCart();
		forManagingShoppingCarts.empty(shoppingCart.getId());
	}

	@CanWriteMyShoppingCart
	@PostMapping("/items")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void addItem(@RequestBody @Valid ShoppingCartItemInput input) {
		ShoppingCartOutput shoppingCart = loadAuthenticatedCustomerShoppingCart();
		input.setShoppingCartId(shoppingCart.getId());
		try {
			forManagingShoppingCarts.addItem(input);
		} catch (ProductNotFoundException e) {
			throw new UnprocessableEntityException(e.getMessage(), e);
		}
	}

	@CanWriteMyShoppingCart
	@DeleteMapping("/items/{itemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeItem(@PathVariable UUID itemId) {
		ShoppingCartOutput shoppingCart = loadAuthenticatedCustomerShoppingCart();
		forManagingShoppingCarts.removeItem(shoppingCart.getId(), itemId);
	}

	private ShoppingCartOutput loadAuthenticatedCustomerShoppingCart() {
		return forQueryingShoppingCarts.findByCustomerId(securityChecks.getAuthenticatedUserId());
	}
}