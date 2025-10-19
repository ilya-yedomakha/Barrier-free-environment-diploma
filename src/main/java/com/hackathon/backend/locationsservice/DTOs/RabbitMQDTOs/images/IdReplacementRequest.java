package com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.images;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class IdReplacementRequest {

    UUID correlationId;
    UUID newId;
    TypeOfImageReplacement typeOfImageReplacement;

}


