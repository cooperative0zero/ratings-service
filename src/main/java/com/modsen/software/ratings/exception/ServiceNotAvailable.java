package com.modsen.software.ratings.exception;

import org.springframework.http.HttpStatus;

public class ServiceNotAvailable extends BaseCustomException {
    public ServiceNotAvailable(String customMessage) {
        super(HttpStatus.SERVICE_UNAVAILABLE.value(), customMessage);
    }
}
