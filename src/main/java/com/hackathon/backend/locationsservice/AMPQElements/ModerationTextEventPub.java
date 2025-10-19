package com.hackathon.backend.locationsservice.AMPQElements;

import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationElementType;
import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ModerationTextEventPub {

    private final AmqpTemplate amqpTemplate;

    @Value("${amqp.exchange.moderation}")
    private String moderationExchange;

    public void sendTextForModeration(String requestId, ModerationElementType type, String text) {

        ModerationRequest request = new ModerationRequest(requestId, type, text);

        amqpTemplate.convertAndSend(
                moderationExchange,
                "moderation.text.request",
                request
        );

        log.info("Sent moderation request: [{}], type [{}]", requestId, type);
    }
}
