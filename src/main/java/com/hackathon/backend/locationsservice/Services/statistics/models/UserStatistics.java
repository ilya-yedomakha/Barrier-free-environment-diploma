package com.hackathon.backend.locationsservice.Services.statistics.models;

public record UserStatistics(
        Integer numberAddedLocations,
        Integer numberApprovedLocations,
        Integer numberLocationsInReview,
        Integer numberAddedChecks
)
{
}
