package com.modsen.software.ratings.controller;

import com.modsen.software.ratings.dto.RatingRequest;
import com.modsen.software.ratings.dto.RatingResponse;
import com.modsen.software.ratings.service.RatingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<RatingResponse>> getAll(@RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
                                                       @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
                                                       @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                       @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        List<RatingResponse> response = ratingService.getAll(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/passengers/{id}")
    public ResponseEntity<List<RatingResponse>> getAllByPassengerId(@PathVariable @Min(1) Long id,
                                                     @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
                                                     @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                     @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        List<RatingResponse> response = ratingService.getAllByPassengerId(id, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/drivers/{id}")
    public ResponseEntity<List<RatingResponse>> getAllByDriverId(@PathVariable @Min(1) Long id,
                                                     @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
                                                     @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                     @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        List<RatingResponse> response = ratingService.getAllByDriverId(id, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getById(@PathVariable @Min(1) Long id) {
        RatingResponse response = ratingService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RatingResponse> save(@RequestBody @Valid RatingRequest rideRequest) {
        RatingResponse response = ratingService.save(rideRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<RatingResponse> update(@RequestBody @Valid RatingRequest rideRequest) {
        RatingResponse response = ratingService.update(rideRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/rating/{rating}")
    public ResponseEntity<RatingResponse> changeRating(@PathVariable @Min(1) Long id, @PathVariable @Min(1) @Max(5) Byte rating) {
        RatingResponse response = ratingService.changeRating(id, rating);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
