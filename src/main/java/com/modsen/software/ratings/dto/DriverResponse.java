package com.modsen.software.ratings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private Long carId;
    private Float rating;
    private BigDecimal balance;
    private String driverStatus;
    private Boolean isDeleted;
}
