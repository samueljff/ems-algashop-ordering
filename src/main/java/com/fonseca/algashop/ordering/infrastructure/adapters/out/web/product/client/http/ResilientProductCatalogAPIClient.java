package com.fonseca.algashop.ordering.infrastructure.adapters.out.web.product.client.http;

import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.BadGatewayException;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.GatewayTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryCircuitBreaker;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryConfig;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.core.retry.RetryException;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.UUID;

import static com.fonseca.algashop.ordering.infrastructure.config.resilience.SpringCircuitBreakerConfig.productCatalogCBId;

@Component
@Slf4j
public class ResilientProductCatalogAPIClient {

    private final ProductCatalogAPIClient productCatalogAPIClient;
    private final FrameworkRetryCircuitBreaker circuitBreaker;

    public ResilientProductCatalogAPIClient(ProductCatalogAPIClient productCatalogAPIClient,
                                            CircuitBreakerFactory<FrameworkRetryConfig, FrameworkRetryConfigBuilder> circuitBreakerFactory) {
        this.productCatalogAPIClient = productCatalogAPIClient;
        this.circuitBreaker = (FrameworkRetryCircuitBreaker) circuitBreakerFactory.create(productCatalogCBId);
    }

    @Cacheable(cacheNames = "algashop:product-catalog-api:v1", key = "#productId")
    @ConcurrencyLimit(10)
    public Optional<ProductResponse> getById(UUID productId) {
        log.info("Trying to load product {}", productId);
        log.info("Product catalog API CB state is {}", circuitBreaker.getCircuitBreakerPolicy().getState());
        try {
            return circuitBreaker.run(() -> loadProduct(productId));
        }  catch (NoFallbackAvailableException e) {
            throw unwrapException(e);
        }
    }

    private RuntimeException unwrapException(NoFallbackAvailableException e) {
        if (e.getCause() instanceof RetryException re) {
            if (re.getCause() instanceof GatewayTimeoutException gte) {
               return gte;
            }

            if (re.getCause() instanceof BadGatewayException bge) {
                return bge;
            }
        }
        return e;
    }

    private Optional<ProductResponse> loadProduct(UUID productId) {
        log.info("Loading product {}", productId);
        try {
            return Optional.ofNullable(productCatalogAPIClient.getById(productId));
        } catch (HttpClientErrorException e) {
            if (!(e instanceof HttpClientErrorException.NotFound)) {
                log.error("Client Http error when load product {}", productId, e);
            }
            return Optional.empty();
        } catch (RestClientException e) {
            throw translateException(e);
        }
    }

    private RuntimeException translateException(RestClientException e) {
        if (e.getCause() instanceof SocketTimeoutException
            || e instanceof ResourceAccessException) {
            return new GatewayTimeoutException("Product Catalog API Timeout", e);
        }

        if (e instanceof HttpClientErrorException) {
            return new BadGatewayException.ClientErrorException("Product Catalog API Bad Gateway", e);
        }

        if (e instanceof HttpServerErrorException) {
            return new BadGatewayException.ServerErrorException("Product Catalog API Bad Gateway", e);
        }

        return new BadGatewayException("Product Catalog API Bad Gateway", e);
    }
}
