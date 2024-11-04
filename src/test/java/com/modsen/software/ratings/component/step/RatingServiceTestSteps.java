package com.modsen.software.ratings.component.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.ratings.client.DriverServiceClient;
import com.modsen.software.ratings.client.PassengerServiceClient;
import com.modsen.software.ratings.dto.DriverResponse;
import com.modsen.software.ratings.dto.PassengerResponse;
import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.integration.AbstractIntegrationTest;
import com.modsen.software.ratings.repository.RatingRepository;
import com.modsen.software.ratings.util.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static com.modsen.software.ratings.util.Constants.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class RatingServiceTestSteps extends AbstractIntegrationTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private ResultActions result;

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

        ratingRepository.saveAll(List.of(rating, anotherRating));
    }

    @SneakyThrows
    @When("I request all rating scores from database through service")
    public void iRequestAllRatingScoresFromDatabaseThroughService() {
        result = mockMvc.perform(get("/api/v1/ratings")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortBy", "id")
                .param("sortOrder", "asc")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Then("all rating scores request complete with code {int} \\(OK)")
    public void allRatingScoresRequestCompleteWithCodeOK(int arg0) {
        result.andExpect(status().is(arg0));
    }

    @SneakyThrows
    @And("first rating scores page should be returned")
    public void firstRatingScoresPageShouldBeReturned() {
        result.andExpect(jsonPath("$.items[0].id", is(rating.getId().intValue())))
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
    @When("I request all rating scores with driver id = {int} from database through service")
    public void iRequestAllRatingScoresWithDriverIdFromDatabaseThroughService(int arg0) {
        result = mockMvc.perform(get("/api/v1/ratings/drivers/{id}", arg0)
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortBy", "id")
                .param("sortOrder", "asc")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Then("all rating scores filtered by driver id request complete with code {int} \\(OK)")
    public void allRatingScoresFilteredByDriverIdRequestCompleteWithCodeOK(int arg0) {
        result.andExpect(status().is(arg0));
    }

    @SneakyThrows
    @And("first page of filtered by driver id = {int} rating scores should be returned")
    public void firstPageOfFilteredByDriverIdRatingScoresShouldBeReturned(int arg0) {
        result.andExpect(jsonPath("$.items[0].id", is(rating.getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(rating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(arg0)))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @SneakyThrows
    @When("I request rating score with id = {int} from database through service")
    public void iRequestRatingScoreWithIdFromDatabaseThroughService(int arg0) {
        result = mockMvc.perform(get("/api/v1/ratings/{id}", arg0)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Then("find by id request complete with code {int} \\(OK) of rating scores")
    public void findByIdRequestCompleteWithCodeOKOfRatingScores(int arg0) {
        result.andExpect(status().is(arg0));
    }

    @SneakyThrows
    @And("returned rating score must be with rating = {int} and initiator should be passenger")
    public void returnedRatingScoreMustBeWithRatingAndInitiatorShouldBePassenger(int arg0) {
        result.andExpect(jsonPath("$.id", is(rating.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(rating.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(rating.getDriverId().intValue())))
                .andExpect(jsonPath("$.rating", is(arg0)))
                .andExpect(jsonPath("$.isByPassenger", is(true)))
                .andExpect(jsonPath("$.comment", is(rating.getComment())));
    }

    @SneakyThrows
    @Then("request complete with code {int}\\(NOT_FOUND) and indicates that specified rating score not found")
    public void requestCompleteWithCodeNOT_FOUNDAndIndicatesThatSpecifiedRatingScoreNotFound(int arg0) {
        result.andExpect(status().is(arg0));
    }

    @SneakyThrows
    @When("I save new rating score with driver id = {int}, passenger id = {int} and rating = {int}")
    public void iSaveNewRatingScoreWithDriverIdPassengerIdAndRating(int arg0, int arg1, int arg2) {
        RatingRequest ratingRequest = Constants.ratingRequest.toBuilder()
                    .id(null)
                    .driverId((long) arg0)
                    .passengerId((long) arg1)
                    .rating((byte) arg2)
                    .build();

        result = mockMvc.perform(post("/api/v1/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ratingRequest)));
    }

    @SneakyThrows
    @Then("saved rating score with driver id = {int}, passenger id = {int} and rating = {int} should be returned")
    public void savedRatingScoreWithDriverIdPassengerIdAndRatingShouldBeReturned(int arg0, int arg1, int arg2) {
        result.andExpect(jsonPath("$.passengerId", is(arg1)))
                .andExpect(jsonPath("$.driverId", is(arg0)))
                .andExpect(jsonPath("$.rating", is(arg2)))
                .andExpect(jsonPath("$.isByPassenger", is(ratingRequest.getIsByPassenger())))
                .andExpect(jsonPath("$.comment", is(ratingRequest.getComment())));
    }

    @SneakyThrows
    @When("I try to update rating score with id = {int} changing rating to {int}")
    public void iTryToUpdateRatingScoreWithIdChangingRatingTo(int arg0, int arg1) {
        result = mockMvc.perform(patch("/api/v1/ratings/{id}/rating/{rating}", arg0, arg1)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Then("updated rating with rating = {int} should be returned")
    public void updatedRatingWithRatingShouldBeReturned(int arg0) {
        result.andExpect(jsonPath("$.rating", is(arg0)));
    }
}
