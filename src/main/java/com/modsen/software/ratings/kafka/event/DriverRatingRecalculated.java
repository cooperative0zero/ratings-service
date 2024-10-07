package com.modsen.software.ratings.kafka.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DriverRatingRecalculated extends BaseRatingEvent {
    private Long driverId;
    private Float newValue;

    public DriverRatingRecalculated(Long driverId, Float newValue) {
        this.driverId = driverId;
        this.newValue = newValue;

        this.type = this.getClass().getSimpleName();
    }
}
