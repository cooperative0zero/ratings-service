package com.modsen.software.ratings.contract;

import com.modsen.software.ratings.client.DriverServiceClient;
import com.modsen.software.ratings.client.PassengerServiceClient;
import com.modsen.software.ratings.dto.DriverResponse;
import com.modsen.software.ratings.dto.PassengerResponse;
import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.integration.AbstractIntegrationTest;
import com.modsen.software.ratings.repository.RatingRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMessageVerifier
@AutoConfigureMockMvc
public class RatingBaseContractTest extends AbstractIntegrationTest {
    private static Rating rating;
    private static Rating secondRating;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerServiceClient passengerServiceClient;

    @MockBean
    private DriverServiceClient driverServiceClient;

    static {
        rating = Rating.builder()
                .id(1L)
                .passengerId(1L)
                .driverId(1L)
                .rating((byte) 1)
                .comment("Comment 1")
                .creationDate(OffsetDateTime.now())
                .isByPassenger(true)
                .build();

        secondRating = Rating.builder()
                .id(2L)
                .passengerId(2L)
                .driverId(2L)
                .rating((byte) 2)
                .comment("Comment 2")
                .creationDate(OffsetDateTime.now())
                .isByPassenger(false)
                .build();
    }

    @PostConstruct
    void setupServiceClientsMocks() {

        when(passengerServiceClient.getPassengerById(1L)).thenReturn(new ResponseEntity<>(new PassengerResponse(
                1L,
                "First Middle Last",
                "example@mail.com",
                "987654321",
                false
        ), HttpStatus.OK));

        when(passengerServiceClient.getPassengerById(2L)).thenReturn(new ResponseEntity<>(new PassengerResponse(
                2L,
                "First2 Middle2 Last2",
                "example2@mail.com",
                "123456789",
                false
        ), HttpStatus.OK));

        when(driverServiceClient.getDriverById(1L)).thenReturn(new ResponseEntity<>(new DriverResponse(
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
        ), HttpStatus.OK));

        when(driverServiceClient.getDriverById(2L)).thenReturn(new ResponseEntity<>(new DriverResponse(
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
        ), HttpStatus.OK));

        this.ratingRepository.saveAll(List.of(rating, secondRating));

        RestAssuredMockMvc.mockMvc(mockMvc);
    }
}
