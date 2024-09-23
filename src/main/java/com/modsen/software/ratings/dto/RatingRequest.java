package com.modsen.software.ratings.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {

    @Min(1)
    private Long id;

    @Min(1)
    private Long driverId;

    @Min(1)
    private Long passengerId;

    @Min(1)
    @Max(5)
    private Byte rating;

    @Size(max = 500)
    private String comment;

    private Date creationDate;
}

