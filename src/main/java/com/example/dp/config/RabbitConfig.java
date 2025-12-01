package com.example.dp.config;
import com.example.dp.filter.RequestContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;

import java.util.UUID;

@Configuration
public class RabbitConfig {

    public static final String QUEUE = "school-class-queue";
    public static final String CORRELATION_ID = "X-Correlation-Id";
    private final RequestContext requestContext;

    public RabbitConfig(RequestContext requestContext){
        this.requestContext = requestContext;
    }

    @Bean
    public Queue createQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setMandatory(true);

        // Intercept messages and add X-Correlation-Id
        template.setBeforePublishPostProcessors(message -> {

            String correlationId = getCorrelationId();
//            String correlationId = MDC.get(CORRELATION_ID);
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }
            message.getMessageProperties().setHeader(CORRELATION_ID, correlationId);
            return message;
        });

        return template;
    }

    private String getCorrelationId() {
        String correlationId = requestContext.getCorrelationId();

        // Fallback to MDC which might be set in async scenarios
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = MDC.get(CORRELATION_ID);
        }

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        return correlationId;
    }
}

