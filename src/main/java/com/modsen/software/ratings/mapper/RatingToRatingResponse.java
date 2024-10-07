package com.modsen.software.ratings.mapper;

import com.modsen.software.ratings.dto.RatingResponse;
import com.modsen.software.ratings.entity.Rating;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RatingToRatingResponse implements Converter<Rating, RatingResponse> {

    @Override
    public RatingResponse convert(Rating source) {
        return RatingResponse.builder()
                .id(source.getId())
                .rating(source.getRating())
                .comment(source.getComment())
                .creationDate(source.getCreationDate())
                .passengerId(source.getPassengerId())
                .isByPassenger(source.getIsByPassenger())
                .driverId(source.getDriverId())
                .build();
    }
}
