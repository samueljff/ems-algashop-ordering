package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/customers/me/shopping-cart/items")
    }
    response {
        status 204
    }
}