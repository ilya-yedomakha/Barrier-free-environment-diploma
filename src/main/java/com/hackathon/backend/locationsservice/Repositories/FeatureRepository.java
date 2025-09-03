package com.hackathon.backend.locationsservice.Repositories;
import com.hackathon.backend.locationsservice.Domain.Feature;
import com.hackathon.backend.locationsservice.Domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, UUID> {
    List<Feature> findAllByLocationId(UUID locationId);
}
