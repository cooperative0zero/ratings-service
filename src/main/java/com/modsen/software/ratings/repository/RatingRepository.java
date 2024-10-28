package com.modsen.software.ratings.repository;

import com.modsen.software.ratings.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> findAllByPassengerId(Long passengerId, Pageable pageable);
    Page<Rating> findAllByDriverId(Long driverId, Pageable pageable);

    @Modifying
    @Query("UPDATE Rating rt SET rt.rating = :rating, creationDate = :creationDate WHERE rt.id = :id")
    int changeRating(@Param("id") Long id, @Param("rating") Byte rating, @Param("creationDate") OffsetDateTime date);
}
