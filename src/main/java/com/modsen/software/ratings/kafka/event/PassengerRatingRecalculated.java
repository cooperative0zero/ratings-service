package com.modsen.software.ratings.kafka.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PassengerRatingRecalculated extends BaseRatingEvent {
    private Long passengerId;
    private Float newValue;

    public PassengerRatingRecalculated(Long passengerId, Float newValue) {
        this.passengerId = passengerId;
        this.newValue = newValue;

        this.type = this.getClass().getSimpleName();
    }
}
