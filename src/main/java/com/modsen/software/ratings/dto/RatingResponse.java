package com.modsen.software.ratings.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Rating response")
public class RatingResponse {
    @Schema(description = "Identifier")
    private Long id;

    @Schema(description = "Driver's identifier")
    private Long driverId;

    @Schema(description = "Passenger's identifier")
    private Long passengerId;

    @Schema(description = "Rating value")
    private Byte rating;

    @Schema(description = "Comment")
    private String comment;

    @Schema(description = "Date of rating creation")
    private OffsetDateTime creationDate;

    @Schema(description = "Determines who the rating was set by")
    private Boolean isByPassenger;
}