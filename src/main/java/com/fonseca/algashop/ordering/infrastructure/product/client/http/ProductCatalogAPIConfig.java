package com.fonseca.algashop.ordering.infrastructure.product.client.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.http.HttpClient;

@Configuration
public class ProductCatalogAPIConfig {

    @Bean
    ProductCatalogAPIClient productCatalogAPIClient(RestClient.Builder builder, @Value("${algashop.integrations.product-catalog.url}")String url){
        RestClient restClient = builder.baseUrl(url).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();
        return proxyFactory.createClient(ProductCatalogAPIClient.class);
    }
}
