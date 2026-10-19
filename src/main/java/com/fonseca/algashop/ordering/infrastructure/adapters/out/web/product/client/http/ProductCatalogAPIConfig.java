package com.fonseca.algashop.ordering.infrastructure.adapters.out.web.product.client.http;

import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;
import java.util.Collections;

@Configuration
public class ProductCatalogAPIConfig {

    private static final String REGISTRATION_ID = "algashop-ordering";

    @Bean
    ProductCatalogAPIClient productCatalogAPIClient(RestClient.Builder builder,
        ProductCatalogIntegrationProperties properties, OAuth2AuthorizedClientManager manager) {

        var interceptor = new OAuth2ClientHttpRequestInterceptor(manager);

        interceptor.setClientRegistrationIdResolver(_ -> properties.getOauth2ClientRegistrationId());
        interceptor.setPrincipalResolver(_ -> generatePrincipal(properties.getOauth2ClientRegistrationId()));

        RestClient restClient = builder.baseUrl(properties.getUrl())
            .requestFactory(generateClientHttpRequestFactory())
            .requestInterceptor(interceptor)
            .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();
        return proxyFactory.createClient(ProductCatalogAPIClient.class);
    }

    private Authentication generatePrincipal(String principalName) {

        return new AbstractAuthenticationToken(Collections.emptySet()) {
            @Override
            public @Nullable Object getPrincipal() {
                return principalName;
            }

            @Override
            public @Nullable Object getCredentials() {
                return null;
            }
        };
    }

    private ClientHttpRequestFactory generateClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(5));
        factory.setConnectTimeout(Duration.ofSeconds(2));
        return factory;
    }
}
