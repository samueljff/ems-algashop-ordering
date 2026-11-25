package contracts.shoppingCartNotFound

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/customers/me/shopping-cart")
    }
    response {
        status 404
        headers {
            contentType("application/problem+json")
        }
        body([
            instance: fromRequest().path(),
            type    : "/errors/not-found",
            title   : "Not Found"
        ])
    }
}