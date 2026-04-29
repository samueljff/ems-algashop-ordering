package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/shopping-carts/b9e23c1d-48fa-4d7b-a365-8c1f05e92b47")
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
            id         : fromRequest().path(3),
            customerId : anyUuid(),
            totalItems : anyPositiveInt(),
            totalAmount: anyNumber(),
            items      : [
                [
                    id         : anyUuid(),
                    productId  : anyUuid(),
                    name       : anyNonBlankString(),
                    price      : anyNumber(),
                    quantity   : anyPositiveInt(),
                    totalAmount: anyNumber(),
                    available  : anyBoolean()
                ],
                [
                    id         : anyUuid(),
                    productId  : anyUuid(),
                    name       : anyNonBlankString(),
                    price      : anyNumber(),
                    quantity   : anyPositiveInt(),
                    totalAmount: anyNumber(),
                    available  : anyBoolean()
                ]
            ]
        ])
    }
}