package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.FeatureDTO;
import com.hackathon.backend.locationsservice.Domain.Feature;
import com.hackathon.backend.locationsservice.Repositories.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;

    public Optional<Feature> getById(UUID featureId) {
        return featureRepository.findById(featureId);
    }

    public List<Feature> getAllFeaturesByLocationId(UUID locationId) {
        return featureRepository.findAllById(locationId);
    }

    private void setFeatureProperties(Feature feature, FeatureDTO featureDTO) {
        if (featureDTO.getType() != null) feature.setType(featureDTO.getType());
        if (featureDTO.getSubtype() != null) feature.setSubtype(featureDTO.getSubtype());
        if (featureDTO.getDescription() != null) feature.setDescription(featureDTO.getDescription());
        if (featureDTO.getStatus() != null) feature.setStatus(featureDTO.getStatus());
        if (featureDTO.getQualityRating() != null) feature.setQualityRating(featureDTO.getQualityRating());
        if (featureDTO.getStandardsCompliance() != null)
            feature.setStandardsCompliance(featureDTO.getStandardsCompliance());
    }

    public Feature addFeature(UUID locationId, FeatureDTO featureDTO) {
        Feature feature = new Feature();
        //TODO created_by
        feature.setCreatedBy(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        feature.setLocationId(locationId);
        setFeatureProperties(feature, featureDTO); // Викликаємо допоміжний метод
        return featureRepository.save(feature);
    }


    public FeatureDTO updateFeature(Feature feature, FeatureDTO featureDTO) {
        setFeatureProperties(feature, featureDTO);
        feature.setUpdatedAt(LocalDateTime.now());
        featureRepository.save(feature);
        return featureDTO;
    }

    public void deleteFeature(UUID featureId) {
        featureRepository.deleteById(featureId);
    }
}
