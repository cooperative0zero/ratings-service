package com.modsen.software.ratings.kafka.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StatusChangedEvent extends BaseRideEvent {
    private String fromStatus;
    private String toStatus;
    private String userType;

    public StatusChangedEvent(Long rideId, String fromStatus, String toStatus, String userType) {
        this.rideId = rideId;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.userType = userType;
        this.type = this.getClass().getSimpleName();
    }
}
