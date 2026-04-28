package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/orders") {
            queryParameters {
                parameter("page", "0")
                parameter("size", "4")
            }
        }
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
            number       : 0,
            size         : 1,
            totalPages   : 1,
            totalElements: 1,
            content      : [
                [
                    id           : "01226N0640J7Q",
                    customer     : [
                        id       : anyUuid(),
                        firstName: "Alice",
                        lastName : "Cooper",
                        document : "67890",
                        email    : "alice.cooper@email.com",
                        phone    : "1199887766"
                    ],
                    totalItems   : 3,
                    totalAmount  : 89.97,
                    placedAt     : anyIso8601WithOffset(),
                    canceledAt   : null,
                    paidAt       : null,
                    readyAt      : null,
                    status       : "PLACED",
                    paymentMethod: "CREDIT_CARD"
                ]
            ]
        ])
    }
}