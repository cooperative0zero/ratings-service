package com.modsen.software.ratings.exception;

import org.springframework.http.HttpStatus;

public class RatingParticipantsNotExists extends BaseCustomException {

    public RatingParticipantsNotExists(String customMessage) {
        super(HttpStatus.BAD_REQUEST.value(), customMessage);
    }
}
