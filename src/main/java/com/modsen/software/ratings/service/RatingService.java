package com.modsen.software.ratings.service;

import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.dto.RatingResponse;

import java.util.List;

public interface RatingService {
    RatingResponse getById(Long id);

    List<RatingResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    List<RatingResponse> getAllByPassengerId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    List<RatingResponse> getAllByDriverId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    RatingResponse save(RatingRequest request);

    RatingResponse update(RatingRequest request);

    RatingResponse changeRating(Long id, Byte rating);
}
