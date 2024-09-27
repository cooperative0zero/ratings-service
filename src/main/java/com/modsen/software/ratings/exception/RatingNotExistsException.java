package com.modsen.software.ratings.exception;

import org.springframework.http.HttpStatus;

public class RatingNotExistsException extends BaseCustomException {
    public RatingNotExistsException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
