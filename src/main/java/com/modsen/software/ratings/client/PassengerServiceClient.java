package com.modsen.software.ratings.client;

import com.modsen.software.ratings.dto.PassengerResponse;
import com.modsen.software.ratings.exception.BaseCustomException;
import com.modsen.software.ratings.exception.ServiceNotAvailable;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "passenger-service")
public interface PassengerServiceClient {

    @GetMapping("/api/v1/passengers/{id}")
    @CircuitBreaker(name = "passengerServiceCircuitBreaker", fallbackMethod = "handleGetPassengerByIdFallback")
    ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id);

    default ResponseEntity<PassengerResponse> handleGetPassengerByIdFallback(Long id, Throwable ex) {
        if (ex instanceof FeignException && ((FeignException) ex).status() == HttpStatus.NOT_FOUND.value()) {
            throw new BaseCustomException(HttpStatus.NOT_FOUND.value(), String.format("Passenger with id = %d not found", id));
        }
        throw new ServiceNotAvailable("Passenger service not available");
    }
}
