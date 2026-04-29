package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/shopping-carts/00000000-0000-0000-0000-000000000000/items")
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