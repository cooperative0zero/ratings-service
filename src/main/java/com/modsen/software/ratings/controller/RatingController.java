package com.modsen.software.ratings.controller;

import com.modsen.software.ratings.dto.PaginatedResponse;
import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.dto.RatingResponse;
import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.service.RatingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Rating controller", description="Required for rating management")
public class RatingController {

    private final RatingServiceImpl ratingService;

    private final ConversionService conversionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all ratings",
            description = "Allows to get ratings values using pagination."
    )
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
    @Operation(
            summary = "Get all ratings by passenger's identifier",
            description = "Allows to get ratings values by passenger's identifier using pagination."
    )
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
    @Operation(
            summary = "Get all ratings by driver's identifier",
            description = "Allows to get ratings values by driver's identifier using pagination."
    )
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
    @Operation(
            summary = "Get rating by identifier",
            description = "Allows to get rating by identifier."
    )
    public RatingResponse getById(@PathVariable @Min(1) Long id) {
        return conversionService.convert(ratingService.getById(id), RatingResponse.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create new rating",
            description = "Allows to create new rating."
    )
    public RatingResponse save(@RequestBody @Valid RatingRequest ratingRequest) {
        var rating = Objects.requireNonNull(conversionService.convert(ratingRequest, Rating.class));
        return conversionService.convert(ratingService.save(rating), RatingResponse.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update rating",
            description = "Allows to update rating."
    )
    public RatingResponse update(@RequestBody @Valid RatingRequest ratingRequest) {
        var rating = Objects.requireNonNull(conversionService.convert(ratingRequest, Rating.class));
        return conversionService.convert(ratingService.update(rating), RatingResponse.class);
    }

    @PatchMapping("/{id}/rating/{rating}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Change rating's status",
            description = "Allows to change rating's status."
    )
    public RatingResponse changeRating(@PathVariable @Min(1) Long id, @PathVariable @Min(1) @Max(5) Byte rating) {
        return conversionService.convert(ratingService.changeRating(id, rating), RatingResponse.class);
    }
}
