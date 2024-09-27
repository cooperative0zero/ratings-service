package com.modsen.software.ratings.client;

import com.modsen.software.ratings.dto.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "127.0.0.1:8080")
public interface PassengerServiceClient {

    @GetMapping("/api/v1/passengers/{id}")
    ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id);
}
