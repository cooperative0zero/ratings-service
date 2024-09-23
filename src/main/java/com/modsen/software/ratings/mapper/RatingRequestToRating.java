package com.modsen.software.ratings.mapper;

import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.entity.Rating;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RatingRequestToRating implements Converter<RatingRequest, Rating> {

    @Override
    public Rating convert(RatingRequest source) {
        return Rating.builder()
                .id(source.getId())
                .rating(source.getRating())
                .comment(source.getComment())
                .creationDate(source.getCreationDate())
                .passengerId(source.getPassengerId())
                .driverId(source.getDriverId())
                .build();
    }
}
