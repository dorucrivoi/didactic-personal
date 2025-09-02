package com.example.dp.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.example.dp.config.RabbitConfig.*;

@Service
public class SchoolClassProducer {
    private static final Logger logger = LoggerFactory.getLogger(SchoolClassProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public SchoolClassProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendClassCreated(SchoolClassCreatedEvent event) {
        try {
            Message message = buildEventMessage(event, "SchoolClassCreatedEvent");
            rabbitTemplate.convertAndSend(
                    QUEUE,
                    message,
                    msg -> {
                        msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return msg;
                    }
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize SchoolClassCreatedEvent", e);
        }
        logger.info(" [x] Sent event: {}", event.getClassCode());
    }

    public void sendClassDeleted(SchoolClassDeletedEvent event) {
        try {
            Message message = buildEventMessage(event, "SchoolClassDeletedEvent");
            rabbitTemplate.convertAndSend(
                    QUEUE,
                    message,
                    msg -> {
                        msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return msg;
                    }
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize SchoolClassCreatedEvent", e);
        }
        logger.info(" [x] Sent event: {}", event.getClassCode());
    }

    private Message buildEventMessage(SchoolClassEvent event, String eventType) throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("classCode", event.getClassCode());
        payload.put("year", event.getYear());

        String jsonMessage = objectMapper.writeValueAsString(payload);

        MessageProperties props = new MessageProperties();
        props.setHeader("type", eventType);
        props.setContentType("application/json");

        return new Message(jsonMessage.getBytes(StandardCharsets.UTF_8), props);
    }
}
