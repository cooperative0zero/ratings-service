package com.modsen.software.ratings.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.ratings.client.DriverServiceClient;
import com.modsen.software.ratings.client.PassengerServiceClient;
import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.repository.RatingRepository;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static com.modsen.software.ratings.util.Constants.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private PassengerServiceClient passengerServiceClient;

    @MockBean
    private DriverServiceClient driverServiceClient;

    @PostConstruct
    void setupServiceClientsMocks() {
        when(passengerServiceClient.getPassengerById(1L)).thenReturn(new ResponseEntity<>(passengerResponse, HttpStatus.OK));
        when(passengerServiceClient.getPassengerById(2L)).thenReturn(new ResponseEntity<>(anotherPassengerResponse, HttpStatus.OK));
        when(driverServiceClient.getDriverById(1L)).thenReturn(new ResponseEntity<>(driverResponse, HttpStatus.OK));
        when(driverServiceClient.getDriverById(2L)).thenReturn(new ResponseEntity<>(anotherDriverResponse, HttpStatus.OK));
    }

    @BeforeEach
    void prepare() {
        jdbcTemplate.execute("TRUNCATE TABLE ratings RESTART IDENTITY;");
    }

    @SneakyThrows
    @Test
    void getAllRatingsWhenRatingsExist() {
        ratingRepository.saveAll(List.of(rating, anotherRating));

        mockMvc.perform(get("/api/v1/ratings")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(rating.getId().intValue())))
                .andExpect(jsonPath("$.items[1].id", is(anotherRating.getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(rating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[1].passengerId", is(anotherRating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(rating.getDriverId().intValue())))
                .andExpect(jsonPath("$.items[1].driverId", is(anotherRating.getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(2)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @SneakyThrows
    @Test
    void getRatingByIdWhenRatingExists() {
        ratingRepository.save(rating);

        mockMvc.perform(get("/api/v1/ratings/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(rating.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(rating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(rating.getDriverId().intValue())))
                .andExpect(jsonPath("$.rating", is(rating.getRating().intValue())))
                .andExpect(jsonPath("$.isByPassenger", is(rating.getIsByPassenger())))
                .andExpect(jsonPath("$.comment", is(rating.getComment())));
    }

    @SneakyThrows
    @Test
    void getAllByPassengerIdWhenRatingsExist() {
        ratingRepository.saveAll(List.of(rating, anotherRating));

        mockMvc.perform(get("/api/v1/ratings/passengers/{id}", 1L)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(rating.getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(rating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(rating.getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @SneakyThrows
    @Test
    void getAllByDriverIdWhenRatingExist() {
        ratingRepository.saveAll(List.of(rating, anotherRating));

        mockMvc.perform(get("/api/v1/ratings/drivers/{id}", 2L)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(anotherRating.getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(anotherRating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(anotherRating.getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @SneakyThrows
    @Test
    void saveRatingWhenRatingNotExists() {
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
    }

    @SneakyThrows
    @Test
    void updateRatingWhenRatingExists() {
        OffsetDateTime now = OffsetDateTime.now();

        RatingRequest ratingRequestToUpdate = ratingRequest.toBuilder()
                .comment("New comment")
                .creationDate(now)
                .isByPassenger(true)
                .build();

        ratingRepository.save(rating);
        mockMvc.perform(put("/api/v1/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ratingRequestToUpdate)))
                .andExpect(jsonPath("$.id", is(ratingRequestToUpdate.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(ratingRequestToUpdate.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(ratingRequestToUpdate.getDriverId().intValue())))
                .andExpect(jsonPath("$.rating", is(ratingRequestToUpdate.getRating().intValue())))
                .andExpect(jsonPath("$.isByPassenger", is(ratingRequestToUpdate.getIsByPassenger())))
                .andExpect(jsonPath("$.comment", is(ratingRequestToUpdate.getComment())));
    }

    @SneakyThrows
    @Test
    void changeRatingWhenRatingExists() {
        ratingRepository.save(rating);

        mockMvc.perform(patch("/api/v1/ratings/{id}/rating/{rating}", 1L, 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(rating.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(rating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(rating.getDriverId().intValue())))
                .andExpect(jsonPath("$.rating", is(5)))
                .andExpect(jsonPath("$.isByPassenger", is(rating.getIsByPassenger())))
                .andExpect(jsonPath("$.comment", is(rating.getComment())));
    }
}
