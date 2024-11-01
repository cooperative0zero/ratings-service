package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        url("/api/v1/ratings/passengers/2")
    }
    response {
        headers {
            contentType: "application/json"
        }
        status 200
        body(
                items: [
                        [
                                id: 2,
                                passengerId: 2,
                                driverId: 2,
                                rating: 2,
                                comment: 'Comment 2',
                                isByPassenger: false
                        ]
                ],
                page: 0,
                size: 10,
                total: 1
        )
    }
}
