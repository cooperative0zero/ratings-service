package com.modsen.software.ratings.repository.impl;

import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.repository.CustomRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
@RequiredArgsConstructor
public class CustomRatingRepositoryImpl implements CustomRatingRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public int changeRating(String id, Byte rating, OffsetDateTime date) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update()
                .set("rt_rating", rating)
                .set("rt_creationDate", date);

        return (int) mongoTemplate.updateFirst(query, update, Rating.class).getModifiedCount();
    }
}
