package com.modsen.software.ratings.kafka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.ratings.kafka.event.BaseRatingEvent;
import com.modsen.software.ratings.kafka.event.DriverRatingRecalculated;
import com.modsen.software.ratings.kafka.event.PassengerRatingRecalculated;
import org.apache.kafka.common.serialization.Serializer;

public class RatingEventSerializer implements Serializer<BaseRatingEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String s, BaseRatingEvent baseRatingEvent) {
        try {
            if (baseRatingEvent instanceof DriverRatingRecalculated) {
                return objectMapper.writeValueAsBytes(baseRatingEvent);
            } else if (baseRatingEvent instanceof PassengerRatingRecalculated) {
                return objectMapper.writeValueAsBytes(baseRatingEvent);
            }

            throw new IllegalArgumentException("Unknown type: " + baseRatingEvent.getClass());
        } catch (Exception e) {
            throw new RuntimeException("Error serializing MyCustomObject", e);
        }
    }
}
