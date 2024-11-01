package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method PUT()
        headers {
            contentType applicationJson()
        }
        url("/api/v1/ratings")
        body(
                id: 1,
                passengerId: 1,
                driverId: 1,
                rating: 1,
                comment: 'Updated Comment 1',
                isByPassenger: true
        )
    }
    response {
        headers {
            contentType: "application/json"
        }
        status 200
        body(
                id: 1,
                passengerId: 1,
                driverId: 1,
                rating: 1,
                comment: 'Updated Comment 1',
                isByPassenger: true
        )
    }
}
