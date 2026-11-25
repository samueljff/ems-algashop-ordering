package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/customers/me/shopping-cart")
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
            id         : anyUuid(),
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