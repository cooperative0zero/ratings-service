package com.modsen.software.ratings.controller;

import com.modsen.software.ratings.dto.PaginatedResponse;
import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.dto.RatingResponse;
import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.service.RatingServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {

    private final RatingServiceImpl ratingService;

    private final ConversionService conversionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<RatingResponse> getAll(
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        var result = ratingService.getAll(pageNumber, pageSize, sortBy, sortOrder)
                .stream()
                .map(rating -> conversionService.convert(rating, RatingResponse.class))
                .toList();

        return new PaginatedResponse<>(result, pageNumber, pageSize, result.size());
    }

    @GetMapping("/passengers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<RatingResponse> getAllByPassengerId(
            @PathVariable @Min(1) Long id,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        var result = ratingService.getAllByPassengerId(id, pageNumber, pageSize, sortBy, sortOrder)
                .stream()
                .map(rating -> conversionService.convert(rating, RatingResponse.class))
                .toList();

        return new PaginatedResponse<>(result, pageNumber, pageSize, result.size());
    }

    @GetMapping("/drivers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<RatingResponse> getAllByDriverId(
            @PathVariable @Min(1) Long id,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        var result = ratingService.getAllByDriverId(id, pageNumber, pageSize, sortBy, sortOrder)
                .stream()
                .map(rating -> conversionService.convert(rating, RatingResponse.class))
                .toList();

        return new PaginatedResponse<>(result, pageNumber, pageSize, result.size());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse getById(@PathVariable @Min(1) Long id) {
        return conversionService.convert(ratingService.getById(id), RatingResponse.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse save(@RequestBody @Valid RatingRequest ratingRequest) {
        var rating = Objects.requireNonNull(conversionService.convert(ratingRequest, Rating.class));
        return conversionService.convert(ratingService.save(rating), RatingResponse.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse update(@RequestBody @Valid RatingRequest ratingRequest) {
        var rating = Objects.requireNonNull(conversionService.convert(ratingRequest, Rating.class));
        return conversionService.convert(ratingService.update(rating), RatingResponse.class);
    }

    @PatchMapping("/{id}/rating/{rating}")
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse changeRating(@PathVariable @Min(1) Long id, @PathVariable @Min(1) @Max(5) Byte rating) {
        return conversionService.convert(ratingService.changeRating(id, rating), RatingResponse.class);
    }
}
