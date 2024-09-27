package com.modsen.software.ratings.service;

import com.modsen.software.ratings.client.DriverServiceClient;
import com.modsen.software.ratings.client.PassengerServiceClient;
import com.modsen.software.ratings.entity.Rating;
import com.modsen.software.ratings.exception.RatingAlreadyExistsException;
import com.modsen.software.ratings.exception.RatingNotExistsException;
import com.modsen.software.ratings.exception.RatingParticipantsNotExists;
import com.modsen.software.ratings.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl {

    private final RatingRepository ratingRepository;

    private final DriverServiceClient driverServiceClient;

    private final PassengerServiceClient passengerServiceClient;


    @Transactional(readOnly = true)
    public Rating getById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(()-> new RatingNotExistsException(String.format("Rating with id = %d not exists", id)));
    }

    @Transactional(readOnly = true)
    public List<Rating> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return ratingRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent();
    }

    @Transactional(readOnly = true)
    public List<Rating> getAllByPassengerId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return ratingRepository.findAllByPassengerId(id, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent();
    }

    @Transactional(readOnly = true)
    public List<Rating> getAllByDriverId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return ratingRepository.findAllByDriverId(id, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent();
    }

    @Transactional
    public Rating save(Rating rating) {
        if (rating.getId() != null && ratingRepository.existsById(rating.getId())) {
            throw new RatingAlreadyExistsException(String.format("Rating with id = %d already exists", rating.getId()));
        } else {
            if (!(driverServiceClient.getDriverById(rating.getDriverId()).getStatusCode() == HttpStatus.OK
                && passengerServiceClient.getPassengerById(rating.getPassengerId()).getStatusCode() == HttpStatus.OK))
                throw new RatingParticipantsNotExists("One or several participants not exist");

            rating.setCreationDate(OffsetDateTime.now());
            ratingRepository.save(rating);
        }

        return rating;
    }

    @Transactional
    public Rating update(Rating request) {
        if (!ratingRepository.existsById(request.getId()))
            throw new RatingNotExistsException(String.format("Rating with id = %d not exists", request.getId()));

        request.setCreationDate(OffsetDateTime.now());

        return ratingRepository.save(request);
    }

    @Transactional
    public Rating changeRating(Long id, Byte rating) {
        if (ratingRepository.changeRating(id, rating, new Date()) == 0)
            throw new RatingNotExistsException(String.format("Rating with id = %d not exists", id));

        return ratingRepository.findById(id).get();
    }
}