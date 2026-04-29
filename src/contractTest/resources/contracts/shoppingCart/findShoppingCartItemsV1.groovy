package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/shopping-carts/b9e23c1d-48fa-4d7b-a365-8c1f05e92b47/items")
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
            items: [
                [
                    id         : anyUuid(),
                    productId  : anyUuid(),
                    name       : "Desktop",
                    price      : 600,
                    quantity   : 2,
                    totalAmount: 1200,
                    available  : true
                ],
                [
                    id         : anyUuid(),
                    productId  : anyUuid(),
                    name       : "Monitor",
                    price      : 300,
                    quantity   : 2,
                    totalAmount: 600,
                    available  : true
                ]
            ]
        ])
    }
}