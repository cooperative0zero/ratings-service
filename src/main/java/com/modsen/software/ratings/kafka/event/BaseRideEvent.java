package com.modsen.software.ratings.kafka.event;

import lombok.Data;

@Data
public abstract class BaseRideEvent {
    protected Long rideId;
    protected String type;
}
