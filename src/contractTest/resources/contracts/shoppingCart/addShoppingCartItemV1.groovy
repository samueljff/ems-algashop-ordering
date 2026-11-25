package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        urlPath("/api/v1/customers/me/shopping-cart/items")
        headers {
            contentType("application/json")
        }
        body([
            productId: value(
                test("f5ab7a1e-37da-41e1-892b-a1d38275c2f2"),
                stub(anyUuid())
            ),
            quantity : value(
                test(2),
                stub(anyPositiveInt())
            )
        ])
    }
    response {
        status 204
    }
}