package com.modsen.software.ratings.repository;

import com.modsen.software.ratings.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String>, CustomRatingRepository {
    Page<Rating> findAllByPassengerId(Long passengerId, Pageable pageable);
    Page<Rating> findAllByDriverId(Long driverId, Pageable pageable);
}
