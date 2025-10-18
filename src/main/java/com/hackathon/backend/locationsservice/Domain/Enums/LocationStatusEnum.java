package com.hackathon.backend.locationsservice.Domain.Enums;

public enum LocationStatusEnum {
    pending, // Location waits reviewing
    published, // Location was reviewed and published
    rejected // Location's content is inappropriate or location is a duplicate
}
