package com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation;

public enum ModerationCategory {
    SAFE,
    TOXIC,
    OFF_TOPIC,
    PII_LEAK,
    SPAM,
    UNKNOWN
}
