package com.modsen.software.ratings.repository;

import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.util.Constants;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.modsen.software.ratings.util.Constants.anotherRating;
import static com.modsen.software.ratings.util.Constants.rating;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class RatingRepositoryTest {

    @Autowired
    private RatingRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("TRUNCATE TABLE ratings RESTART IDENTITY;");
    }

    @Test
    void saveWhenRatingNotExists() {
        Rating savedRating = repository.save(rating);

        assertNotNull(savedRating);
        assertEquals(rating.getId(), savedRating.getId());
        assertEquals(rating.getPassengerId(), savedRating.getPassengerId());
        assertEquals(rating.getDriverId(), savedRating.getDriverId());
        assertEquals(rating.getComment(), savedRating.getComment());
    }

    @Test
    void updateWhenRatingExists() {
        repository.save(rating);

        Rating rating = Constants.rating.toBuilder().build();
        rating.setComment("Updated comment");
        rating.setPassengerId(2L);

        Rating updatedRating = repository.save(rating);
        assertEquals(rating.getId(), updatedRating.getId());
        assertEquals(rating.getPassengerId(), updatedRating.getPassengerId());
        assertEquals(rating.getDriverId(), updatedRating.getDriverId());
        assertEquals(rating.getComment(), updatedRating.getComment());
    }

    @Test
    void changeRatingWhenRatingExists() {
        OffsetDateTime now = OffsetDateTime.now();
        repository.save(rating);

        int linesChanged = repository.changeRating(rating.getId(), (byte) 4, now);
        assertEquals(1, linesChanged);

        entityManager.clear();

        Optional<Rating> updated = repository.findById(rating.getId());

        assertTrue(updated.isPresent());
        assertEquals(((byte) 4), updated.get().getRating());
        assertEquals(now.toEpochSecond(), updated.get().getCreationDate().toEpochSecond());
    }

    @Test
    void findByIdWhenRatingExists() {
        Rating savedRating = repository.save(rating);

        Optional<Rating> foundRating = repository.findById(savedRating.getId());
        assertTrue(foundRating.isPresent());
        assertEquals(savedRating.getComment(), foundRating.get().getComment());
    }

    @Test
    void findAllWhenRatingsExist() {
        repository.save(rating);
        repository.save(anotherRating);

        List<Rating> ratings = repository.findAll();
        assertEquals(2, ratings.size());
        assertEquals(rating.getId(), ratings.get(0).getId());
        assertEquals(anotherRating.getId(), ratings.get(1).getId());
    }

    @Test
    void findAllByPassengerIdWhenRatingsExist() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.ASC,
                "id"
        );

        repository.save(rating);
        repository.save(anotherRating);

        List<Rating> ratings = repository.findAllByPassengerId(rating.getPassengerId(), pageable).getContent();

        assertEquals(1, ratings.size());
        assertEquals(rating.getId(), ratings.get(0).getId());
    }

    @Test
    void findAllByDriverIdWhenRatingsExist() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.ASC,
                "id"
        );

        repository.save(rating);
        repository.save(anotherRating);

        List<Rating> ratings = repository.findAllByDriverId(anotherRating.getDriverId(), pageable).getContent();

        assertEquals(1, ratings.size());
        assertEquals(anotherRating.getId(), ratings.get(0).getId());
    }
}
