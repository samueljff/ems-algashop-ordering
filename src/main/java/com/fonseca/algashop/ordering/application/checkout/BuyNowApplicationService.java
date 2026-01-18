package com.fonseca.algashop.ordering.application.checkout;

import com.fonseca.algashop.ordering.domain.model.commons.Quantity;
import com.fonseca.algashop.ordering.domain.model.commons.ZipCode;
import com.fonseca.algashop.ordering.domain.model.customer.CustomerId;
import com.fonseca.algashop.ordering.domain.model.order.*;
import com.fonseca.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.fonseca.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.fonseca.algashop.ordering.domain.model.product.Product;
import com.fonseca.algashop.ordering.domain.model.product.ProductCatalogService;
import com.fonseca.algashop.ordering.domain.model.product.ProductId;
import com.fonseca.algashop.ordering.domain.model.product.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BuyNowApplicationService {

    private final BuyNowService buyNowService;
    private final ProductCatalogService productCatalogService;

    private final ShippingCostService shippingCostService;
    private final OriginAddressService originAddressService;

    private final Orders orders;

    private final ShippingInputDisassembler shippingInputDisassembler;
    private final BillingInputDisassembler billingInputDisassembler;

    @Transactional
    public String buyNow(BuyNowInput input) {
        Objects.requireNonNull(input);

        PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());
        CustomerId customerId = new CustomerId(input.getCustomerId());
        Quantity quantity = new Quantity(input.getQuantity());

        Product product = findProduct(new ProductId(input.getProductId()));

        var shippingCalculationResult = calculateShippingCost(input.getShipping());

        Shipping shipping = shippingInputDisassembler.toDomainModel(input.getShipping(),
                shippingCalculationResult);

        Billing billing = billingInputDisassembler.toDomainModel(input.getBilling());

        Order order = buyNowService.buyNow(
                product, customerId, billing, shipping, quantity, paymentMethod
        );

        orders.add(order);

        return order.id().toString();
    }

    private ShippingCostService.CalculationResult calculateShippingCost(ShippingInput shipping) {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode(shipping.getAddress().getZipCode());
        return shippingCostService.calculate(new ShippingCostService.CalculationRequest(origin, destination));
    }

    private Product findProduct(ProductId productId) {
        return productCatalogService.ofId(productId)
                .orElseThrow(()-> new ProductNotFoundException());
    }
}
