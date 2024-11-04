package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        headers {
            contentType applicationJson()
        }
        url("/api/v1/ratings")
        body(
                [
                        passengerId: 1,
                        driverId: 1,
                        rating: 1,
                        comment: 'Comment 1',
                        isByPassenger: true
                ]
        )
    }
    response {
        status 201
        body(
                id: 3,
                passengerId: 1,
                driverId: 1,
                rating: 1,
                comment: 'Comment 1',
                isByPassenger: true
        )
    }
}
