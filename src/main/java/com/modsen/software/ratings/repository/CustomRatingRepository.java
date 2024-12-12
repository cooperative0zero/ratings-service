package com.modsen.software.ratings.repository;

import java.time.OffsetDateTime;

public interface CustomRatingRepository {
    int changeRating(String id, Byte rating, OffsetDateTime date);
}
