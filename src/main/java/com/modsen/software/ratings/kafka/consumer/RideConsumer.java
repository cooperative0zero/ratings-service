package com.modsen.software.ratings.kafka.consumer;

import com.modsen.software.ratings.kafka.event.BaseRideEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RideConsumer {

    @KafkaListener(topics = {"ride_events"}, groupId = "ratingConsumerGroup")
    public void listenGroup(BaseRideEvent message) throws Exception {
        System.out.println(message);
    }
}
