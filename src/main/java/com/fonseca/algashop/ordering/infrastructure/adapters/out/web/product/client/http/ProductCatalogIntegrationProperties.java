package com.fonseca.algashop.ordering.infrastructure.adapters.out.web.product.client.http;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Data
@ConfigurationProperties(prefix = "algashop.integrations.product-catalog")
public class ProductCatalogIntegrationProperties {
    @NotBlank
    private String url;
    @NotBlank
    private String oauth2ClientRegistrationId;
}