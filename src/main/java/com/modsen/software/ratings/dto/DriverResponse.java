package com.modsen.software.ratings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverResponse {
    private Long id;
    private String fullName;
    private String email;
    private String gender;
    private Long carId;
    private Boolean isDeleted;
}
