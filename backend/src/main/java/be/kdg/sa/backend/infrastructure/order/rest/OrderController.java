package be.kdg.sa.backend.infrastructure.order.rest;

import be.kdg.sa.backend.domain.Order;
import be.kdg.sa.backend.domain.OrderRepository;
import be.kdg.sa.backend.infrastructure.config.RabbitMQTopology;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("order")
public class OrderController {
    private final RabbitTemplate rabbitTemplate;

    public OrderController(RabbitTemplate rabbitTemplate, OrderRepository orderRepository) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/place/delivery")
    public void placeDelivery(@RequestBody Order order) {
        rabbitTemplate.convertAndSend(RabbitMQTopology.ORDER_EXCHANGE_NAME);
    }
}
