package com.modsen.software.ratings.service.impl;

import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.dto.RatingResponse;
import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.exception.RatingAlreadyExistsException;
import com.modsen.software.ratings.exception.RatingNotExistsException;
import com.modsen.software.ratings.repository.RatingRepository;
import com.modsen.software.ratings.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public RatingResponse getById(Long id) {
        var rating = ratingRepository.findById(id)
                .orElseThrow(()-> new RatingNotExistsException(String.format("Rating with id = %d not exists", id)));
        return conversionService.convert(rating, RatingResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return ratingRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent()
                .stream()
                .map((o)-> conversionService.convert(o, RatingResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponse> getAllByPassengerId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return ratingRepository.findAllByPassengerId(id, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent()
                .stream()
                .map((o)-> conversionService.convert(o, RatingResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponse> getAllByDriverId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return ratingRepository.findAllByDriverId(id, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent()
                .stream()
                .map((o)-> conversionService.convert(o, RatingResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public RatingResponse save(RatingRequest request) {
        var rating = Objects.requireNonNull(conversionService.convert(request, Rating.class));

        if (rating.getId() != null && ratingRepository.existsById(rating.getId())) {
            throw new RatingAlreadyExistsException(String.format("Rating with id = %d already exists", rating.getId()));
        } else {
            // send request to rides-service and wait the response
            rating.setCreationDate(new Date());
            ratingRepository.save(rating);
        }

        return conversionService.convert(rating, RatingResponse.class);
    }

    @Override
    @Transactional
    public RatingResponse update(RatingRequest request) {
        if (!ratingRepository.existsById(request.getId()))
            throw new RatingNotExistsException(String.format("Rating with id = %d not exists", request.getId()));

        var updated = Objects.requireNonNull(conversionService.convert(request, Rating.class));
        request.setCreationDate(new Date());

        return conversionService.convert(ratingRepository.save(updated), RatingResponse.class);
    }

    @Override
    @Transactional
    public RatingResponse changeRating(Long id, Byte rating) {
        if (ratingRepository.changeRating(id, rating, new Date()) == 0)
            throw new RatingNotExistsException(String.format("Rating with id = %d not exists", id));

        return conversionService.convert(ratingRepository.findById(id).get(), RatingResponse.class);
    }
}