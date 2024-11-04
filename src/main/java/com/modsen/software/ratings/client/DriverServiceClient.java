package com.modsen.software.ratings.client;

import com.modsen.software.ratings.dto.DriverResponse;
import com.modsen.software.ratings.exception.BaseCustomException;
import com.modsen.software.ratings.exception.ServiceNotAvailable;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "driver-service")
public interface DriverServiceClient {

    @GetMapping("/api/v1/drivers/{id}")
    @CircuitBreaker(name = "driverServiceCircuitBreaker", fallbackMethod = "handleGetDriverByIdFallback")
    ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id);

    default ResponseEntity<DriverResponse> handleGetDriverByIdFallback(Long id, Throwable ex) {
        if (ex instanceof FeignException && ((FeignException) ex).status() == HttpStatus.NOT_FOUND.value()) {
            throw new BaseCustomException(HttpStatus.NOT_FOUND.value(), String.format("Driver with id = %d not found", id));
        }
        throw new ServiceNotAvailable("Driver service not available");
    }
}
