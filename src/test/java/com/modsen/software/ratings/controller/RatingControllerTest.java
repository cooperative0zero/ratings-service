package com.modsen.software.ratings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.modsen.software.ratings.client.DriverServiceClient;
import com.modsen.software.ratings.client.PassengerServiceClient;
import com.modsen.software.ratings.dto.*;
import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.service.RatingServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static com.modsen.software.ratings.util.Constants.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RatingController.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerServiceClient passengerServiceClient;

    @MockBean
    private DriverServiceClient driverServiceClient;

    @MockBean
    private RatingServiceImpl ratingService;

    private PaginatedResponse<RatingResponse> paginatedResponse;
    private List<RatingResponse> ratingResponses;
    private List<Rating> ratings;

    @PostConstruct
    void setupServiceClientsMocks() {

        when(passengerServiceClient.getPassengerById(1L)).thenReturn(new ResponseEntity<>(passengerResponse, HttpStatus.OK));

        when(passengerServiceClient.getPassengerById(2L)).thenReturn(new ResponseEntity<>(anotherPassengerResponse, HttpStatus.OK));

        when(driverServiceClient.getDriverById(1L)).thenReturn(new ResponseEntity<>(driverResponse, HttpStatus.OK));

        when(driverServiceClient.getDriverById(2L)).thenReturn(new ResponseEntity<>(anotherDriverResponse, HttpStatus.OK));

        ratings = List.of(rating, anotherRating);
        ratingResponses = List.of(ratingResponse, anotherRatingResponse);

        paginatedResponse = new PaginatedResponse<>(
                ratingResponses,
                0,
                10,
                ratings.size()
        );
    }

    @SneakyThrows
    @Test
    void getAllWhenRatingsExist() {
        when(ratingService.getAll(any(), any(), any(), any()))
                .thenReturn(ratings);

        mockMvc.perform(get("/api/v1/ratings")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(ratingResponses.get(0).getId().intValue())))
                .andExpect(jsonPath("$.items[1].id", is(ratingResponses.get(1).getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(ratingResponses.get(0).getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[1].passengerId", is(ratingResponses.get(1).getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(ratingResponses.get(0).getDriverId().intValue())))
                .andExpect(jsonPath("$.items[1].driverId", is(ratingResponses.get(1).getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(paginatedResponse.getTotal())))
                .andExpect(jsonPath("$.page", is(paginatedResponse.getPage())))
                .andExpect(jsonPath("$.size", is(paginatedResponse.getSize())));

        verify(ratingService, times(1)).getAll(any(), any(), any(), any());
    }

    @SneakyThrows
    @Test
    void getAllByPassengerIdWhenRatingsExist() {
        when(ratingService.getAllByPassengerId(argThat(i -> i.compareTo(1L) == 0), any(), any(), any(), any()))
                .thenReturn(ratings.stream().filter(rating -> rating.getPassengerId().compareTo(1L) == 0).toList());

        mockMvc.perform(get("/api/v1/ratings/passengers/{id}", 1L)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(ratingResponses.get(0).getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(ratingResponses.get(0).getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(ratingResponses.get(0).getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(paginatedResponse.getPage())))
                .andExpect(jsonPath("$.size", is(paginatedResponse.getSize())));

        verify(ratingService, times(1)).getAllByPassengerId(argThat(i -> i.compareTo(1L) == 0), any(), any(), any(), any());
    }

    @SneakyThrows
    @Test
    void getAllByDriverIdWhenRatingExists() {
        when(ratingService.getAllByDriverId(argThat(i -> i.compareTo(2L) == 0), any(), any(), any(), any()))
                .thenReturn(ratings.stream().filter(rating -> rating.getDriverId().compareTo(2L) == 0).toList());

        mockMvc.perform(get("/api/v1/ratings/drivers/{id}", 2L)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(ratingResponses.get(1).getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(ratingResponses.get(1).getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(ratingResponses.get(1).getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(paginatedResponse.getPage())))
                .andExpect(jsonPath("$.size", is(paginatedResponse.getSize())));

        verify(ratingService, times(1)).getAllByDriverId(argThat(i -> i.compareTo(2L) == 0), any(), any(), any(), any());
    }

    @SneakyThrows
    @Test
    void getByIdWhenRatingExists() {
        when(ratingService.getById(1L))
                .thenReturn(rating);

        mockMvc.perform(get("/api/v1/ratings/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(rating.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(rating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(rating.getDriverId().intValue())))
                .andExpect(jsonPath("$.rating", is(rating.getRating().intValue())))
                .andExpect(jsonPath("$.isByPassenger", is(rating.getIsByPassenger())))
                .andExpect(jsonPath("$.comment", is(rating.getComment())));

        verify(ratingService, times(1)).getById(1L);
    }

    @SneakyThrows
    @Test
    void saveWhenRatingNotExists() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(ratingService.save(argThat(rating -> rating.getId().compareTo(1L) == 0))).thenReturn(rating);

        mockMvc.perform(post("/api/v1/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ratingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(rating.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(rating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(rating.getDriverId().intValue())))
                .andExpect(jsonPath("$.rating", is(rating.getRating().intValue())))
                .andExpect(jsonPath("$.isByPassenger", is(rating.getIsByPassenger())))
                .andExpect(jsonPath("$.comment", is(rating.getComment())));

        verify(ratingService, times(1)).save(argThat(rating -> rating.getId().compareTo(1L) == 0));
    }

    @SneakyThrows
    @Test
    void updateWhenRatingExists() {
        OffsetDateTime now = OffsetDateTime.now();

        RatingRequest ratingRequestToUpdate = ratingRequest.toBuilder()
                .comment("New comment")
                .creationDate(now)
                .isByPassenger(true)
                .build();

        Rating ratingToUpdate = rating.toBuilder()
                .comment("New comment")
                .creationDate(now)
                .isByPassenger(true)
                .build();

        RatingResponse ratingUpdatedResponse = ratingResponse.toBuilder()
                .comment("New comment")
                .creationDate(now)
                .isByPassenger(true)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(ratingService.update(argThat(rating -> rating.getId().compareTo(1L) == 0))).thenReturn(ratingToUpdate);

        mockMvc.perform(put("/api/v1/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ratingRequestToUpdate)))
                .andExpect(jsonPath("$.id", is(ratingUpdatedResponse.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(ratingUpdatedResponse.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(ratingUpdatedResponse.getDriverId().intValue())))
                .andExpect(jsonPath("$.rating", is(ratingUpdatedResponse.getRating().intValue())))
                .andExpect(jsonPath("$.isByPassenger", is(ratingUpdatedResponse.getIsByPassenger())))
                .andExpect(jsonPath("$.comment", is(ratingUpdatedResponse.getComment())));

    }

    @SneakyThrows
    @Test
    void changeRatingWhenRatingExists() {
        Rating updatedRating = rating.toBuilder()
                .rating((byte) 5)
                .build();

        when(ratingService.changeRating(argThat(arg -> arg.compareTo(1L) == 0), anyByte())).thenReturn(updatedRating);

        mockMvc.perform(patch("/api/v1/ratings/{id}/rating/{rating}", 1L, 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(rating.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(rating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(rating.getDriverId().intValue())))
                .andExpect(jsonPath("$.rating", is(5)))
                .andExpect(jsonPath("$.isByPassenger", is(rating.getIsByPassenger())))
                .andExpect(jsonPath("$.comment", is(rating.getComment())));

        verify(ratingService, times(1)).changeRating(argThat(arg -> arg.compareTo(1L) == 0), anyByte());
    }
}
