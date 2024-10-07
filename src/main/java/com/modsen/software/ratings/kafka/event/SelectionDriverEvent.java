package com.modsen.software.ratings.kafka.event;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SelectionDriverEvent extends BaseRideEvent{
    public SelectionDriverEvent(Long rideId) {
        this.rideId = rideId;
        this.type = this.getClass().getSimpleName();
    }
}
