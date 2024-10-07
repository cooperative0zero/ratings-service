package com.modsen.software.ratings.kafka.event;

import lombok.Data;

@Data
public abstract class BaseRatingEvent {
    protected String type;
}
