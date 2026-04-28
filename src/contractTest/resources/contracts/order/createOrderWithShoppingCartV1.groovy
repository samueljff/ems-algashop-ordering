package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url "/api/v1/orders"
        headers {
            contentType("application/vnd.order-with-shopping-cart.v1+json")
        }
        body([
            shoppingCartId: value(test(anyUuid()), stub(anyUuid())),
            paymentMethod: "GATEWAY_BALANCE",
            shipping     : [
                recipient: [
                    firstName: value(test("John"), stub(nonBlank())),
                    lastName : value(test("Doe"), stub(nonBlank())),
                    document : value(test("112-33-2321"), stub(nonBlank())),
                    phone    : value(test("1191234564"), stub(nonBlank())),
                ],
                address  : [
                    street      : "Bourbon Street",
                    number      : "1234",
                    complement  : "apt. 11",
                    neighborhood: "North Ville",
                    city        : "Montfort",
                    state       : "South Carolina",
                    zipCode     : "79911"
                ]
            ],
            billing      : [
                firstName: value(test("John"), stub(nonBlank())),
                lastName : value(test("Doe"), stub(nonBlank())),
                phone    : value(test("123-111-9911"), stub(nonBlank())),
                document : value(test("225-09-1992"), stub(nonBlank())),
                email    : value(test("johndoe@email.com"), stub(nonBlank())),
                address  : [
                    street      : "Bourbon Street",
                    number      : "1234",
                    complement  : "apt. 11",
                    neighborhood: "North Ville",
                    city        : "Montfort",
                    state       : "South Carolina",
                    zipCode     : "79911"
                ]
            ]
        ])
    }
    response {
        status 201
        headers {
            contentType('application/json')
        }
        body([
            id: anyNonBlankString(),
            customer: [
                id: anyUuid(),
                firstName: fromRequest().body('$.shipping.recipient.firstName'),
                lastName : fromRequest().body('$.shipping.recipient.lastName'),
                document : "12345",
                email    : fromRequest().body('$.billing.email'),
                phone    : fromRequest().body('$.shipping.recipient.phone')
            ],
            totalItems: 2,
            totalAmount: 41.98,
            placedAt: anyIso8601WithOffset(),
            status: "PLACED",
            paymentMethod: "GATEWAY_BALANCE",
            shipping: [
                cost: 20.5,
                expectedDate: anyDate(),
                recipient: [
                    firstName: fromRequest().body('$.shipping.recipient.firstName'),
                    lastName : fromRequest().body('$.shipping.recipient.lastName'),
                    document: "12345",
                    phone: "5511912341234"
                ],
                address: [
                    street: "Bourbon Street",
                    number: "2000",
                    complement: "apt 122",
                    neighborhood: "North Ville",
                    city: "Yostfort",
                    state: "South Carolina",
                    zipCode: "12321"
                ]
            ],
            billing: [
                firstName: fromRequest().body('$.billing.firstName'),
                lastName : fromRequest().body('$.billing.lastName'),
                document: "12345",
                phone: "5511912341234",
                address: [
                    street: "Bourbon Street",
                    number: "2000",
                    complement: "apt 122",
                    neighborhood: "North Ville",
                    city: "Yostfort",
                    state: "South Carolina",
                    zipCode: "12321"
                ]
            ],
            items: [
                [
                    id: anyNonBlankString(),
                    orderId: anyNonBlankString(),
                    productId: anyUuid(),
                    productName: "Notebook Dive Gamer X11",
                    price: 19.99,
                    quantity: 2,
                    totalAmount: 41.98
                ]
            ]
        ])
    }
}