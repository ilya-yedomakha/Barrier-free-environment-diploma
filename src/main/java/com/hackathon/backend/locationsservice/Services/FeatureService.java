package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.FeatureDTO;
import com.hackathon.backend.locationsservice.Domain.Feature;
import com.hackathon.backend.locationsservice.Repositories.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;

    public List<Feature> getAllFeaturesByLocationId(UUID locationId) {
        return featureRepository.findAllById(locationId);
    }

    private void setFeatureProperties(Feature feature, FeatureDTO featureDTO) {
        feature.setType(featureDTO.getType());
        feature.setSubtype(featureDTO.getSubtype());
        feature.setDescription(featureDTO.getDescription());
        feature.setStatus(featureDTO.getStatus());
        feature.setQualityRating(featureDTO.getQualityRating());
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


//
//    public FeatureDTO updateFeature(UUID locationId, UUID featureId, FeatureDTO featureDTO) {
//    }
//
//    public void deleteFeature(UUID locationId, UUID featureId) {
//        featureRepository.deleteById(featureId);
//        Feature feature = featureRepository.findById(featureId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        //TODO user priviliges
//        featureRepository.delete(feature);
//    }
//
//
//    public Feature findById(UUID featureId) {
//        return featureRepository.findById(featureId).orElseThrow(EntityNotFoundException::new);;
//    }
}
