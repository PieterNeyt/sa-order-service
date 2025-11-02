package be.kdg.sa.backend.infrastructure.handler;

import be.kdg.sa.backend.application.OrderService;
import be.kdg.sa.backend.infrastructure.config.RabbitMQTopology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestaurantHandler {

    private final OrderService orderService;

    public RestaurantHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.order-accept}")
    public void receiveAccepterOrderResponse(RestaurantResponse msg) {
        orderService.orderAccepted(msg);
    }

    @RabbitListener(queues = "${rabbitmq.queue.order-deny}")
    public void receiveDeniedOrderResponse(RestaurantResponse msg) {
        orderService.orderDenied(msg);
    }

    @RabbitListener(queues = "${rabbitmq.queue.order-ready}")
    public void receiveReadyOrderResponse(RestaurantResponse msg) {
        orderService.orderReady(msg);
    }

    @RabbitListener(queues = "${rabbitmq.queue.pickedup}")
    public void receivePickedUpDeliveryResponse(RestaurantResponse msg) {
        orderService.orderPickedUp(msg);
    }
    @RabbitListener(queues = "${rabbitmq.queue.deliverd}")
    public void receiveDeliveredDeliveryResponse(RestaurantResponse msg) {
        orderService.orderDeliverd(msg);
    }
    @RabbitListener(queues = "${rabbitmq.queue.deliverd}")
    public void receiveClaimedDeliveryResponse(RestaurantResponse msg) {
        orderService.orderClaimed(msg);
    }
}
