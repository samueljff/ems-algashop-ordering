package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url "/api/v1/orders"
        headers {
            accept "application/json"
            contentType "application/vnd.order-with-product.v1+json"
        }

        body([
            productId    : value(test(anyUuid()), stub(anyUuid())),
            customerId   : value(test(anyUuid()), stub(anyUuid())),
            quantity     : value(test(1), stub(anyPositiveInt())),
            paymentMethod: "",
            shipping     : [
                recipient: [
                    firstName: "",
                    lastName : "Doe",
                    document : "112-33-2321",
                    phone    : "111-441-1244"
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
                firstName: "John",
                lastName : "Doe",
                phone    : "123-111-9911",
                document : "225-09-1992",
                email    : "jhon.doe@gmail.com",
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