package com.fonseca.algashop.ordering.core.domain.model.order.shipping;

import com.fonseca.algashop.ordering.core.domain.model.AbstractDomainIT;
import com.fonseca.algashop.ordering.core.domain.model.commons.ZipCode;
import com.fonseca.algashop.ordering.core.domain.model.order.shipping.ShippingCostService.CalculationRequest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ShippingCostServiceIT extends AbstractDomainIT {

    @Autowired
    private ShippingCostService shippingCostService;

    @Autowired
    private OriginAddressService originAddressService;

    private WireMockServer wireMockRapidex;

    @BeforeEach
    void setup() {
        setupRapidexWireMock();

        wireMockRapidex.start();
    }

    private void setupRapidexWireMock() {
        wireMockRapidex = new WireMockServer(WireMockConfiguration.options()
            .port(8780)
            .usingFilesUnderDirectory("src/test/resources/wiremock/rapidex")
            .extensions(new ResponseTemplateTransformer(true))
        );
    }

    @AfterEach
    void tearDown() {
        wireMockRapidex.stop();
    }

    @Test
    void shouldCalculate() {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode("12345");

        var calculate = shippingCostService
                .calculate(new CalculationRequest(origin, destination));

        Assertions.assertThat(calculate.cost()).isNotNull();
        Assertions.assertThat(calculate.expectedDate()).isNotNull();
    }

}