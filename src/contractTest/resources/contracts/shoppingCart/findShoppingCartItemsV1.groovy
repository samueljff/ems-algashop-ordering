package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/customers/me/shopping-cart/items")
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