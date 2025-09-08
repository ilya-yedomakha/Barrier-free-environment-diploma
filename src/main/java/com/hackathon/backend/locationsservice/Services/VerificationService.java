package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.Domain.Verification;
import com.hackathon.backend.locationsservice.Repositories.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;

    public Optional<Verification> getById(UUID verificationId) {
        return verificationRepository.findById(verificationId);
    }


    public List<Verification> getAllVerificationsById(UUID locationId){
        return verificationRepository.findAllById(locationId);
    }

    public void deleteById(UUID verificationId){
        verificationRepository.deleteById(verificationId);
    }


//    public Verification addVerification(UUID locationId, Boolean status, String comment, UUID evidencePhotoId, UUID featureId, UUID verifiedById, UUID organizationId) {
//        Verification verification = new Verification();
//        verification.setLocationId(locationId);
//        verification.setStatus(status);
//        verification.setComment(comment);
//        verification.setEvidencePhotoId(evidencePhotoId);
//        verification.setFeatureId(featureId);
//        verification.setVerifiedById(verifiedById);
//        verification.setOrganizationId(organizationId);
//        verification.setCreatedAt(LocalDateTime.now());
//        return verificationRepository.save(verification);
//    }
//    public Verification add(VerificationDTO verificationDTO){
//
//       return verificationRepository.save(verification);
//    }
}
