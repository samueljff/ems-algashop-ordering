package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url "/api/v1/shopping-carts"
        headers {
            contentType("application/json")
        }
        body([
            customerId: null
        ])
    }
    response {
        status 400
        headers {
            contentType("application/problem+json")
        }
        body([
            instance: fromRequest().path(),
            type    : "/errors/invalid-fields",
            title   : "Invalid fields",
            detail  : "One or more fields are invalid",
            fields  : [
                customerId: anyNonBlankString()
            ]
        ])
    }
}