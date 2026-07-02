package com.fonseca.algashop.ordering.core.application.checkout;

import com.fonseca.algashop.ordering.core.application.order.BillingInputDisassembler;
import com.fonseca.algashop.ordering.core.application.order.ShippingInputDisassembler;
import com.fonseca.algashop.ordering.core.domain.model.CreditCardId;
import com.fonseca.algashop.ordering.core.domain.model.DomainException;
import com.fonseca.algashop.ordering.core.domain.model.commons.ZipCode;
import com.fonseca.algashop.ordering.core.domain.model.customer.Customer;
import com.fonseca.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.customer.Customers;
import com.fonseca.algashop.ordering.core.domain.model.order.*;
import com.fonseca.algashop.ordering.core.domain.model.order.shipping.OriginAddressService;
import com.fonseca.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import com.fonseca.algashop.ordering.core.domain.model.product.Product;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductCatalogService;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductId;
import com.fonseca.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartId;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.fonseca.algashop.ordering.core.domain.model.shoppingcart.ShoppingCarts;
import com.fonseca.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import com.fonseca.algashop.ordering.core.ports.in.checkout.ForBuyingWithShoppingCart;
import com.fonseca.algashop.ordering.core.ports.in.order.ShippingInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CheckoutApplicationService implements ForBuyingWithShoppingCart {

    private final ShoppingCarts shoppingCarts;
    private final CheckoutService checkoutService;

    private final BillingInputDisassembler billingInputDisassembler;
    private final ShippingInputDisassembler shippingInputDisassembler;

    private final ShippingCostService shippingCostService;
    private final OriginAddressService originAddressService;
    private final ProductCatalogService productCatalogService;
    private final Orders orders;
    private final Customers customers;

    @Transactional
    @Override
    public String checkout(CheckoutInput input) {
        Objects.requireNonNull(input);

        PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());

        CreditCardId creditCardId = null;

        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            if (input.getCreditCardId() == null) {
                throw new DomainException("Credit card id is required");
            }
            creditCardId = new CreditCardId(input.getCreditCardId());
        }

        ShoppingCartId shoppingCartId = new ShoppingCartId(input.getShoppingCartId());

        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId).orElseThrow(() -> new ShoppingCartNotFoundException());

        Customer customer = customers.ofId(shoppingCart.customerId()).orElseThrow(() -> new CustomerNotFoundException());

        var calculationResult = calculateShippingCost(input.getShipping());

        Billing billing = billingInputDisassembler.toDomainModel(input.getBilling());
        Shipping shipping = shippingInputDisassembler.toDomainModel(input.getShipping(), calculationResult);

        Order order = checkoutService.checkout(customer, shoppingCart, billing, shipping, paymentMethod, creditCardId);

        orders.add(order);
        shoppingCarts.add(shoppingCart);

        return order.id().toString();
    }

    private ShippingCostService.CalculationResult calculateShippingCost(ShippingInput shippingInput) {
        ZipCode originZipcode = originAddressService.originAddress().zipCode();
        ZipCode destinationZipCode = new ZipCode(shippingInput.getAddress().getZipCode());
        return shippingCostService.calculate(new ShippingCostService.CalculationRequest(originZipcode, destinationZipCode));
    }

    private Product findProduct(ProductId productId) {
        return productCatalogService.ofId(productId)
            .orElseThrow(() -> new ProductNotFoundException());
    }
}
