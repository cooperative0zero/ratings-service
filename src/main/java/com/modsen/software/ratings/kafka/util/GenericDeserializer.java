package com.modsen.software.ratings.kafka.util;

import com.modsen.software.ratings.kafka.configuration.KafkaTopics;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class GenericDeserializer extends JsonDeserializer<Object> {

    public GenericDeserializer() {}

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data)
    {
        switch (topic)
        {
            case KafkaTopics.RIDES_TOPIC:
                try (RideDeserializer rideDeserializer = new RideDeserializer()) {
                    return rideDeserializer.deserialize(topic, data);
                }
            case KafkaTopics.DRIVER_TOPIC:
                try (DriverDeserializer driverDeserializer = new DriverDeserializer()) {
                    return driverDeserializer.deserialize(topic, data);
                }
        }
        return super.deserialize(topic, data);
    }
}
