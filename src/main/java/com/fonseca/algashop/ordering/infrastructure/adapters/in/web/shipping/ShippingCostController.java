package com.fonseca.algashop.ordering.infrastructure.adapters.in.web.shipping;

import com.fonseca.algashop.ordering.core.application.shipping.ShippingApplicationService;
import com.fonseca.algashop.ordering.core.application.shipping.ShippingCostPreviewInput;
import com.fonseca.algashop.ordering.core.application.shipping.ShippingCostPreviewOutput;
import com.fonseca.algashop.ordering.infrastructure.config.security.SecurityAnnotations.CanReadOrders;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShippingCostController {

    private final ShippingApplicationService shippingApplicationService;

    @PostMapping("/api/v1/shipping-cost-previews")
    @CanReadOrders
    public ShippingCostPreviewOutput previewShippingCost(@RequestBody @Valid ShippingCostPreviewInput input) {
        return shippingApplicationService.previewCost(input);
    }

}