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
            customerId: value(
                test("f5ab7a1e-37da-41e1-892b-a1d38275c2f2"),
                stub(anyUuid())
            )
        ])
    }
    response {
        status 201
        headers {
            contentType('application/json')
        }
        body([
            id         : anyUuid(),
            customerId : anyUuid(),
            totalItems : 2,
            totalAmount: 1800,
            items      : [
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