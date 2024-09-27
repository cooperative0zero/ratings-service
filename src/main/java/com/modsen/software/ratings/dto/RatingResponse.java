package com.modsen.software.ratings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingResponse {
    private Long id;

    private Long driverId;

    private Long passengerId;

    private Byte rating;

    private String comment;

    private OffsetDateTime creationDate;

    private Boolean isByPassenger;
}