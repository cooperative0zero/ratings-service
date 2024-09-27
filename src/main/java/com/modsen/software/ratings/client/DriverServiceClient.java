package com.modsen.software.ratings.client;

import com.modsen.software.ratings.dto.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "")
public interface DriverServiceClient {

    @GetMapping("/api/v1/drivers/{id}")
    ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id);
}
