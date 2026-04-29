package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/shopping-carts/b9e23c1d-48fa-4d7b-a365-8c1f05e92b47/items/00000000-0000-0000-0000-000000000000")
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