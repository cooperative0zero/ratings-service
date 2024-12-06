package com.modsen.software.ratings.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Rating request")
public class RatingRequest {

    @Min(1)
    @Schema(description = "Identifier")
    private String id;

    @Min(1)
    @NotNull
    @Schema(description = "Driver's identifier")
    private Long driverId;

    @Min(1)
    @NotNull
    @Schema(description = "Passenger's identifier")
    private Long passengerId;

    @Min(1)
    @Max(5)
    @Schema(description = "Rating value")
    private Byte rating;

    @Size(max = 500)
    @Schema(description = "Comment")
    private String comment;

    @Schema(description = "Date of rating creation", hidden = true)
    private OffsetDateTime creationDate;

    @NotNull
    @Schema(description = "Determines who the rating was set by")
    private Boolean isByPassenger;
}