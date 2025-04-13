package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.VerificationDTO;
import com.hackathon.backend.locationsservice.Domain.Verification;
import com.hackathon.backend.locationsservice.Repositories.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;

    public List<Verification> getAll(){
        return verificationRepository.findAll();
    }

    public void deleteById(UUID verificationId){
        verificationRepository.deleteById(verificationId);
    }

//    public Verification add(VerificationDTO verificationDTO){
//
////        return verificationRepository.save(verification);
//    }

//    public Verification update(Verification verification){
//
//    }
}
//