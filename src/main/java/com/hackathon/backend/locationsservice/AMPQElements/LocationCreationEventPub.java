package com.hackathon.backend.locationsservice.AMPQElements;

import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.images.IdReplacementRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocationCreationEventPub {

    private final AmqpTemplate amqpTemplate;
    private final String changeIdTopicExchange;

    public LocationCreationEventPub(final AmqpTemplate amqpTemplate,
                                    @Value("${amqp.exchange.change-correlation-id}")
                             final String changeIdTopicExchange) {
        this.amqpTemplate = amqpTemplate;
        this.changeIdTopicExchange = changeIdTopicExchange;
    }

    public void locationCreated(final IdReplacementRequest idReplacementRequest) {

        String routingKey = "change_id.location";

        amqpTemplate.convertAndSend(
                changeIdTopicExchange,
                routingKey,
                idReplacementRequest
        );
    }

}