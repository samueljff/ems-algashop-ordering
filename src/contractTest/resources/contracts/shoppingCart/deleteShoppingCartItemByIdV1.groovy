package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/customers/me/shopping-cart/items/f5ab7a1e-37da-41e1-892b-a1d38275c2f2")
    }
    response {
        status 204
    }
}