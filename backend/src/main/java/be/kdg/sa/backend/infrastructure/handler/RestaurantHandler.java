package be.kdg.sa.backend.infrastructure.handler;

import be.kdg.sa.backend.infrastructure.config.RabbitMQTopology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestaurantHandler {

    @RabbitListener(queues = RabbitMQTopology.ORDER_ACCEPT_QUEUE)
    public void receiveAccepterOrderResponse(RestaurantResponse msg) {
        log.info("Received accepted Order Message: {}", msg);
    }

    @RabbitListener(queues = RabbitMQTopology.ORDER_DENY_QUEUE)
    public void receiveDeniedOrderResponse(RestaurantResponse msg) {
        log.info("Received denied Order Message: {}", msg);
    }

    @RabbitListener(queues = RabbitMQTopology.ORDER_READY_QUEUE)
    public void receiveReadyOrderResponse(RestaurantResponse msg) {
        log.info("Received Ready Order Message: {}", msg);
    }

    @RabbitListener(queues = RabbitMQTopology.ORDER_RESPONSE_PICKEDUP_QUEUE_NAME)
    public void receivePickedUpDeliveryResponse(RestaurantResponse msg) {
        log.info("Received pickedUp Order Message: {}", msg);
    }
    @RabbitListener(queues = RabbitMQTopology.ORDER_RESPONSE_DELIVERD_QUEUE_NAME)
    public void receiveDeliveredDeliveryResponse(RestaurantResponse msg) {
        log.info("Received deliverd Order Message: {}", msg);
    }
}
