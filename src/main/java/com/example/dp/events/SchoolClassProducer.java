package com.example.dp.events;

import com.example.dp.administration.controller.ProfessorController;

import com.example.dp.config.RabbitConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class SchoolClassProducer {
    private static final Logger logger = LoggerFactory.getLogger(SchoolClassProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public SchoolClassProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendClassCreated(String classCode, int year)  {
        // Create JSON as String
        try{
        Map<String, Object> payload = new HashMap<>();
        payload.put("classCode", classCode);
        payload.put("year", year);

        String jsonMessage = objectMapper.writeValueAsString(payload);
        MessageProperties props = new MessageProperties();
        props.setContentType("application/json");
        Message message = new Message(jsonMessage.getBytes(StandardCharsets.UTF_8), props);

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, message);
    } catch (JsonProcessingException e) {
        throw new RuntimeException("Failed to serialize SchoolClassCreatedEvent", e);
    }
        logger.info(" [x] Sent event: {}", classCode);
    }
}
