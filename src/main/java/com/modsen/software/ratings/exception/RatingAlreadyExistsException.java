package com.modsen.software.ratings.exception;

import org.springframework.http.HttpStatus;

public class RatingAlreadyExistsException extends BaseCustomException{

    public RatingAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
