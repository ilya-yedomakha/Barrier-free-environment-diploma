package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.Domain.Review;
import com.hackathon.backend.locationsservice.Repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> getAllReviewsByLocationId(UUID locationId){
        return reviewRepository.findAllById(locationId);
    }

}
