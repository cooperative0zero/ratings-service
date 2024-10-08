package com.modsen.software.ratings.kafka.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.ratings.kafka.event.BaseRideEvent;
import com.modsen.software.ratings.kafka.event.SelectionDriverEvent;
import com.modsen.software.ratings.kafka.event.StatusChangedEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class RideDeserializer implements Deserializer<BaseRideEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BaseRideEvent deserialize(String topic, byte[] data) {
        try {
            Map<String, Object> map = objectMapper.readValue(data, new TypeReference<>() {});
            String type = (String) map.get("type");

            if ("SelectionDriverEvent".contentEquals(type)) {
                return objectMapper.readValue(data, SelectionDriverEvent.class);
            } else if ("StatusChangedEvent".contentEquals(type)) {
                return objectMapper.readValue(data, StatusChangedEvent.class);
            }
            throw new IllegalArgumentException("Unknown type: " + type);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing ", e);
        }
    }
}

