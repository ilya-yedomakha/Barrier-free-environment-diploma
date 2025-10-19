package com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModerationResponse {
    private String requestId;
    private ModerationElementType elementType;
    private ModerationResult result;
}
