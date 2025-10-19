package com.hackathon.backend.locationsservice.Configurations;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfiguration {

//    @Bean
//    public TopicExchange changeIdTopicExchange(
//            @Value("${amqp.exchange.change-correlation-id}") final String exchangeName) {
//        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
}
