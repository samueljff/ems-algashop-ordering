package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url "/api/v1/orders"
        headers {
            accept "application/json"
            contentType "application/vnd.order-with-shopping-cart.v1+json"
        }

        body([
            shoppingCartId: value(test(anyUuid()), stub(anyUuid())),
            paymentMethod: " ",
            shipping     : [
                recipient: [
                    firstName: " ",
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
        status 400
        headers {
            contentType "application/problem+json"
        }
        body([
            instance: fromRequest().path(),
            type    : "/errors/invalid-fields",
            title   : "Invalid fields",
            detail  : "One or more fields are invalid",
            fields  : [
                paymentMethod               : anyNonBlankString(),
                "shipping.recipient.firstName": anyNonBlankString()
            ]
        ])
    }
}