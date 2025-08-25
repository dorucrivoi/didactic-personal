package com.example.dp.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;


@Configuration
public class RabbitConfig {

    public static final String QUEUE = "school-class-created-queue";

    @Bean
    public Queue queue() {
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

        // ConfirmCallback: broker acks/nacks message persistence
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                //logger
                System.out.println("✅ Message confirmed by broker: " + correlationData);
            } else {
                //logger
                System.err.println("❌ Message NOT confirmed: " + cause);
            }
        });

        return template;
    }
}

