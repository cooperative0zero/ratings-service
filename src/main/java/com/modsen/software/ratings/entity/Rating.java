package com.modsen.software.ratings.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "ratings")
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rt_id")
    private Long id;

    @Column(name = "rt_driver_id", nullable = false)
    private Long driverId;

    @Column(name = "rt_passenger_id", nullable = false)
    private Long passengerId;

    @Column(name = "rt_rating", nullable = false)
    private Byte rating;

    @Column(name = "rt_comment")
    private String comment;

    @Column(name = "rt_creation_date", nullable = false)
    private Date creationDate;
}