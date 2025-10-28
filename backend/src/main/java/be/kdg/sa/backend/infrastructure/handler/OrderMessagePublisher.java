package be.kdg.sa.backend.infrastructure.handler;

import be.kdg.sa.backend.domain.order.IOrderMessagePublisher;
import be.kdg.sa.backend.infrastructure.config.RabbitMQTopology;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderMessagePublisher implements IOrderMessagePublisher {
    @Value("${rabbit.order.exchange}")
    private String ORDER_EXCHANGE_NAME;
    private final RabbitTemplate rabbitTemplate;

    public OrderMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void placeOrder(OrderMessage orderMessage) {
        this.rabbitTemplate.convertAndSend(
                ORDER_EXCHANGE_NAME,
                "order.place." + orderMessage.id(),
                orderMessage
        );
    }
}
