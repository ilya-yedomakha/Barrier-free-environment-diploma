package com.hackathon.backend.locationsservice.Repositories;
import com.hackathon.backend.locationsservice.Domain.Location;
import com.hackathon.backend.locationsservice.Domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findAllById(UUID locationId);
}
