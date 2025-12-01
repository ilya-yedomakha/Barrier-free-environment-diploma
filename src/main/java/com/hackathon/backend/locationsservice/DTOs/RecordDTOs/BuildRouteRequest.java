package com.hackathon.backend.locationsservice.DTOs.RecordDTOs;

import java.util.UUID;

public record BuildRouteRequest(
        Integer height,
        Double lat,
        Double lng,
        UUID route_key
) {}
