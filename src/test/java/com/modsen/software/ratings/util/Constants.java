package com.modsen.software.ratings.util;

import com.modsen.software.ratings.dto.DriverResponse;
import com.modsen.software.ratings.dto.PassengerResponse;
import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.dto.RatingResponse;
import com.modsen.software.ratings.entity.Rating;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Constants {
    public static final Rating rating;
    public static final Rating anotherRating;
    public static final RatingRequest ratingRequest;
    public static final RatingResponse ratingResponse;
    public static final RatingResponse anotherRatingResponse;

    public static final PassengerResponse passengerResponse;
    public static final PassengerResponse anotherPassengerResponse;

    public static final DriverResponse driverResponse;
    public static final DriverResponse anotherDriverResponse;

    static {
        rating = Rating.builder()
                .id(1L)
                .passengerId(1L)
                .driverId(1L)
                .rating((byte) 5)
                .comment("Comment 1")
                .creationDate(OffsetDateTime.now())
                .isByPassenger(true)
                .build();

        anotherRating = Rating.builder()
                .id(2L)
                .passengerId(2L)
                .driverId(2L)
                .rating((byte) 2)
                .comment("Comment 2")
                .creationDate(OffsetDateTime.now())
                .isByPassenger(false)
                .build();

        ratingRequest = new RatingRequest();
        ratingRequest.setId(1L);
        ratingRequest.setPassengerId(1L);
        ratingRequest.setDriverId(1L);
        ratingRequest.setRating(((byte) 1));
        ratingRequest.setComment("Comment 1");
        ratingRequest.setCreationDate(OffsetDateTime.now());
        ratingRequest.setIsByPassenger(true);

        ratingResponse = RatingResponse.builder()
                .id(1L)
                .passengerId(1L)
                .driverId(1L)
                .rating((byte) 1)
                .comment("Comment 1")
                .creationDate(OffsetDateTime.now())
                .isByPassenger(true)
                .build();

        anotherRatingResponse = RatingResponse.builder()
                .id(2L)
                .passengerId(2L)
                .driverId(2L)
                .rating((byte) 2)
                .comment("Comment 2")
                .creationDate(OffsetDateTime.now())
                .isByPassenger(false)
                .build();

        passengerResponse = new PassengerResponse(
                1L,
                "First Middle Last",
                "example@mail.com",
                "987654321",
                false
        );

        anotherPassengerResponse = new PassengerResponse(
                2L,
                "First2 Middle2 Last2",
                "example2@mail.com",
                "123456789",
                false
        );

        driverResponse = new DriverResponse(
                1L,
                "First Middle Last",
                "example@email.com",
                "987654321",
                "MALE",
                0L,
                1f,
                new BigDecimal(0),
                "AVAILABLE",
                false
        );

        anotherDriverResponse = new DriverResponse(
                2L,
                "First2 Middle2 Last2",
                "example2@email.com",
                "123465789",
                "MALE",
                0L,
                2f,
                new BigDecimal(0),
                "AVAILABLE",
                false
        );
    }
}
