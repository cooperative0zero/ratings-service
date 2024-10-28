package com.modsen.software.ratings.kafka.event;

import lombok.Data;

@Data
public abstract class BaseDriverEvent {
    protected Long driverId;
    protected String type;
}
