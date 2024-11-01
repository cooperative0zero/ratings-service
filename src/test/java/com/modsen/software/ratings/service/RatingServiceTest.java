package com.modsen.software.ratings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.modsen.software.ratings.client.DriverServiceClient;
import com.modsen.software.ratings.client.PassengerServiceClient;
import com.modsen.software.ratings.dto.DriverResponse;
import com.modsen.software.ratings.dto.PassengerResponse;
import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.modsen.software.ratings.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private PassengerServiceClient passengerServiceClient;

    @Mock
    private DriverServiceClient driverServiceClient;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void saveWhenRatingNotExists() {
        when(passengerServiceClient.getPassengerById(1L)).thenReturn(new ResponseEntity<>(passengerResponse, HttpStatus.OK));
        when(driverServiceClient.getDriverById(1L)).thenReturn(new ResponseEntity<>(driverResponse, HttpStatus.OK));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(ratingRepository.existsById(rating.getId())).thenReturn(false);

        Rating savedRating = ratingService.save(rating);
        assertNotNull(savedRating);
        assertEquals(rating.getId(), savedRating.getId());
        assertEquals(rating.getDriverId(), savedRating.getDriverId());
        assertEquals(rating.getPassengerId(), savedRating.getPassengerId());
        assertEquals(rating.getComment(), savedRating.getComment());
    }

    @Test
    void updateWhenRatingExists() {
        OffsetDateTime now = OffsetDateTime.now();

        Rating ratingToUpdate = rating.toBuilder()
                .comment("New comment")
                .creationDate(now)
                .isByPassenger(true)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(passengerServiceClient.getPassengerById(1L)).thenReturn(new ResponseEntity<>(passengerResponse, HttpStatus.OK));
        when(driverServiceClient.getDriverById(1L)).thenReturn(new ResponseEntity<>(driverResponse, HttpStatus.OK));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(ratingRepository.existsById(rating.getId())).thenReturn(true);

        Rating updatedRating = ratingService.update(ratingToUpdate);

        assertNotNull(updatedRating);
        assertEquals(ratingToUpdate.getId(), updatedRating.getId());
        assertEquals(ratingToUpdate.getDriverId(), updatedRating.getDriverId());
        assertEquals(ratingToUpdate.getPassengerId(), updatedRating.getPassengerId());
        assertEquals(ratingToUpdate.getComment(), updatedRating.getComment());
    }

    @Test
    void getByIdWhenRatingExists() {
        when(ratingRepository.findById(rating.getId())).thenReturn(Optional.of(rating));

        Rating result = ratingService.getById(1L);
        assertEquals(rating.getId(), result.getId());
        assertEquals(rating.getDriverId(), result.getDriverId());
        assertEquals(rating.getPassengerId(), result.getPassengerId());
        assertEquals(rating.getComment(), result.getComment());
    }

    @Test
    void getAllWhenRatingExist() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Rating> toReturn = List.of(rating, anotherRating);

        when(ratingRepository.findAll(pageable)).thenReturn(new PageImpl<>(toReturn));

        List<Rating> result = ratingService.getAll(pageable.getPageNumber(), pageable.getPageSize(), "id", Sort.Direction.ASC.name());

        assertEquals(2L, result.size());
        assertEquals(rating.getId(), result.get(0).getId());
        assertEquals(anotherRating.getId(), result.get(1).getId());
    }

    @Test
    void getAllByPassengerIdWhenRatingExist() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Rating> toReturn = List.of(rating, anotherRating);

        when(ratingRepository.findAllByPassengerId(rating.getPassengerId(), pageable))
                .thenReturn(new PageImpl<>(toReturn.stream()
                        .filter(item -> item.getPassengerId().compareTo(rating.getPassengerId()) == 0)
                        .toList()));

        List<Rating> result = ratingService.getAllByPassengerId(rating.getPassengerId(), pageable.getPageNumber(), pageable.getPageSize(), "id", Sort.Direction.ASC.name());

        assertEquals(1, result.size());
        assertEquals(rating.getId(), result.get(0).getId());
    }

    @Test
    void getAllByDriverIdWhenRatingsExist() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Rating> toReturn = List.of(rating, anotherRating);

        when(ratingRepository.findAllByDriverId(anotherRating.getDriverId(), pageable))
                .thenReturn(new PageImpl<>(toReturn.stream()
                        .filter(item -> item.getDriverId().compareTo(anotherRating.getDriverId()) == 0)
                        .toList()));

        List<Rating> result = ratingService.getAllByDriverId(anotherRating.getDriverId(), pageable.getPageNumber(), pageable.getPageSize(), "id", Sort.Direction.ASC.name());

        assertEquals(1, result.size());
        assertEquals(anotherRating.getId(), result.get(0).getId());
    }

    @Test
    void changeRatingWhenRatingExists() {
        Rating updatedRating = rating.toBuilder()
                .rating((byte) 5)
                .build();

        when(ratingRepository.changeRating(argThat(arg -> arg.compareTo(1L) == 0), anyByte(), any(OffsetDateTime.class))).thenReturn(1);
        when(ratingRepository.findById(argThat(arg -> arg.compareTo(1L) == 0))).thenReturn(Optional.of(updatedRating));

        Rating result = ratingService.changeRating(rating.getId(), (byte) 5);

        assertEquals(rating.getId(), result.getId());
        assertEquals(rating.getDriverId(), result.getDriverId());
        assertEquals(rating.getPassengerId(), result.getPassengerId());
        assertEquals(rating.getComment(), result.getComment());
        assertEquals(((byte) 5), result.getRating());
    }
}
