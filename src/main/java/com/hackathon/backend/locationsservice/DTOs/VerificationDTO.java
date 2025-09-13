package com.hackathon.backend.locationsservice.DTOs;

import java.util.Optional;
import java.util.UUID;

public class VerificationDTO {
    private boolean status;
    private String comment;
    private Optional<UUID> evidencePhotoId;
    private Optional<UUID> featureId;
}
