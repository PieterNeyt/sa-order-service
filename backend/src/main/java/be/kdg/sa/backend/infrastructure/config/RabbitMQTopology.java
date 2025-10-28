package be.kdg.sa.backend.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {
    @Value("${rabbitmq.exchange.order}")
    private String ORDER_EXCHANGE_NAME;

    @Value("${rabbitmq.exchange.restaurant-response}")
    private String RESTAURANT_RESPONSE_EXCHANGE_NAME;

    @Value("${rabbitmq.exchange.delivery-response}")
    private String DELIVERY_RESPONSE_EXCHANGE_NAME;

    @Value("${rabbitmq.queue.order-accept}")
    private String ORDER_ACCEPT_QUEUE;

    @Value("${rabbitmq.queue.order-deny}")
    private String ORDER_DENY_QUEUE;

    @Value("${rabbitmq.queue.order-ready}")
    private String ORDER_READY_QUEUE;

    @Value("${rabbitmq.queue.pickedup}")
    private String PICKEDUP_QUEUE_NAME;

    @Value("${rabbitmq.queue.deliverd}")
    private String DELIVERD_QUEUE_NAME;

    @Value("${rabbitmq.routing-key.order-accept}")
    private String ORDER_ACCEPT_ROUTING_KEY;

    @Value("${rabbitmq.routing-key.order-deny}")
    private String ORDER_DENY_ROUTING_KEY;

    @Value("${rabbitmq.routing-key.order-ready}")
    private String ORDER_READY_ROUTING_KEY;

    @Value("${rabbitmq.routing-key.order-pickedup}")
    private String ORDER_PICKEDUP_ROUTING_KEY;

    @Value("${rabbitmq.routing-key.order-deliverd}")
    private String ORDER_DELIVERD_ROUTING_KEY;

    //sturen van msg naar restaurant service
    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE_NAME, true, false);
    }

    //ontvangen van berichten van restaurant service
    @Bean
    TopicExchange restaurantResponseExchange() {
        return new TopicExchange(RESTAURANT_RESPONSE_EXCHANGE_NAME, true, false);
    }

    //ontvangen van berichten van restaurant service
    @Bean
    TopicExchange deliveryResponseExchange() {
        return new TopicExchange(DELIVERY_RESPONSE_EXCHANGE_NAME, true, false);
    }

    //ontvangen antwooden van bestellingen die zijn geacepteert
    @Bean
    Queue orderAcceptResponseQueue() {
        return QueueBuilder.nonDurable(ORDER_ACCEPT_QUEUE).build();
    }


    //ontvangen antwooden van bestellingen die zijn gedenied
    @Bean
    Queue orderDeneidResponseQueue() {
        return QueueBuilder.nonDurable(ORDER_DENY_QUEUE).build();
    }

    @Bean
    Queue orderReadyResponseQueue() {
        return QueueBuilder.nonDurable(ORDER_READY_QUEUE).build();
    }

    @Bean
    Queue deliveryPickedupResponseQueue() {
        return QueueBuilder.nonDurable(PICKEDUP_QUEUE_NAME).build();
    }

    @Bean
    Queue deliveryDeliverdResponseQueue() {
        return QueueBuilder.nonDurable(DELIVERD_QUEUE_NAME).build();
    }



    //alles wat die binnent krijgt in restaurant exange, als die key accept heeft naar de accept que als die de key deny heeft naar denyed que
    @Bean
    Binding acceptQueueBinding() {
        return BindingBuilder.bind(orderAcceptResponseQueue())
                .to(restaurantResponseExchange())
                .with(ORDER_ACCEPT_ROUTING_KEY);
    }

    @Bean
    Binding denyQueueBinding() {
        return BindingBuilder.bind(orderDeneidResponseQueue())
                .to(restaurantResponseExchange())
                .with(ORDER_DENY_ROUTING_KEY);
    }

    @Bean
    Binding readyQueueBinding() {
        return BindingBuilder.bind(orderReadyResponseQueue())
                .to(restaurantResponseExchange())
                .with(ORDER_READY_ROUTING_KEY);
    }

    @Bean
    Binding pickedUpQueueBinding() {
        return BindingBuilder.bind(deliveryPickedupResponseQueue())
                .to(deliveryResponseExchange())
                .with(ORDER_PICKEDUP_ROUTING_KEY);
    }@Bean
    Binding deliverdQueueBinding() {
        return BindingBuilder.bind(deliveryDeliverdResponseQueue())
                .to(deliveryResponseExchange())
                .with(ORDER_DELIVERD_ROUTING_KEY);
    }
}

