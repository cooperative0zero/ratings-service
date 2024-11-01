package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        url("/api/v1/ratings/drivers/1")
    }
    response {
        headers {
            contentType: "application/json"
        }
        status 200
        body(
                items: [
                        [
                                id: 3,
                                passengerId: 1,
                                driverId: 1,
                                rating: 1,
                                comment: 'Comment 1',
                                isByPassenger: true
                        ],
                        [
                                id: 1,
                                passengerId: 1,
                                driverId: 1,
                                rating: 1,
                                comment: 'Comment 1',
                                isByPassenger: true
                        ]
                ],
                page: 0,
                size: 10,
                total: 2
        )
    }
}
