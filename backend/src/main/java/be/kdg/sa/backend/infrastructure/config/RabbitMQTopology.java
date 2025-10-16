package be.kdg.sa.backend.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {
    public static final String ORDER_EXCHANGE_NAME = "order-exchange";
    public static final String RESTAURANT_RESPONSE_EXCHANGE_NAME = "restaurant-response-exchange";
    public static final String DELIVERY_RESPONSE_EXCHANGE_NAME = "delivery-response-exchange";

    public static final String ORDER_ACCEPT_QUEUE = "order-accept-queue";
    public static final String ORDER_DENY_QUEUE = "order-deny-queue";
    public static final String ORDER_READY_QUEUE = "order-ready-queue";

    public static final String PICKEDUP_QUEUE_NAME = "pickedup-response-queue";
    public static final String DELIVERD_QUEUE_NAME = "deliverd-response-queue";

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
                .with("order.accept.*");
    }

    @Bean
    Binding denyQueueBinding() {
        return BindingBuilder.bind(orderDeneidResponseQueue())
                .to(restaurantResponseExchange())
                .with("order.deny.*");
    }

    @Bean
    Binding readyQueueBinding() {
        return BindingBuilder.bind(orderReadyResponseQueue())
                .to(restaurantResponseExchange())
                .with("order.ready.*");
    }

    @Bean
    Binding pickedUpQueueBinding() {
        return BindingBuilder.bind(deliveryPickedupResponseQueue())
                .to(deliveryResponseExchange())
                .with("order.pickedup.*");
    }@Bean
    Binding deliverdQueueBinding() {
        return BindingBuilder.bind(deliveryDeliverdResponseQueue())
                .to(deliveryResponseExchange())
                .with("order.deliverd.*");
    }
}

