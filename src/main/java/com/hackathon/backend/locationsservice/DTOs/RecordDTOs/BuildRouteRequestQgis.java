package com.hackathon.backend.locationsservice.DTOs.RecordDTOs;

import java.util.UUID;

public record BuildRouteRequestQgis(
        int height,
        String start,
        UUID route_key
) {}

