package com.modsen.software.ratings.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document("ratings")
@Builder(toBuilder = true)
public class Rating {
    @Id
    @Field(name = "rt_id")
    private String id;

    @Field(name = "rt_driver_id")
    private Long driverId;

    @Field(name = "rt_passenger_id")
    private Long passengerId;

    @Field(name = "rt_rating")
    private Byte rating;

    @Field(name = "rt_comment")
    private String comment;

    @Field(name = "rt_creation_date")
    private OffsetDateTime creationDate;

    @Field(name = "rt_is_by_passenger")
    private Boolean isByPassenger;
}
