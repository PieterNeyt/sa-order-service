package be.kdg.sa.backend.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String ORDER_EXCHANGE_NAME = "order-exchange";
    public static final String ORDER_QUEUE_NAME = "order-queue";

    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE_NAME);
    }

    @Bean
    Queue orderQueue() {
        return QueueBuilder.nonDurable(ORDER_QUEUE_NAME).build();
    }

    @Bean
    Binding OrderQueToPlaceExgangeBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("order.place.*");
    }
}
