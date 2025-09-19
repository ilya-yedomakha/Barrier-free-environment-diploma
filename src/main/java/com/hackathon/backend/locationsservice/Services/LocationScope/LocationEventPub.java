package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.IdReplacementRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocationEventPub {

    private final AmqpTemplate amqpTemplate;
    private final String changeIdTopicExchange;

    public LocationEventPub(final AmqpTemplate amqpTemplate,
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