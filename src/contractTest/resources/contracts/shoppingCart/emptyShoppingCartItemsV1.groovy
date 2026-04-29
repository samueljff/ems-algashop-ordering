package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/shopping-carts/b9e23c1d-48fa-4d7b-a365-8c1f05e92b47/items")
    }
    response {
        status 204
    }
}