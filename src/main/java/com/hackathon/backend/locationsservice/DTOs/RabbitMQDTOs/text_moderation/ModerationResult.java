package com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ModerationResult {

    private String elementId;
    private ModerationElementType elementType;
    private double toxicity;
    private double offTopic;
    private double piiLeak;
    private ModerationCategory category;
    private String explanation;
}

