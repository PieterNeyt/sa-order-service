package be.kdg.sa.backend.domain.order;


import be.kdg.sa.backend.infrastructure.handler.OrderMessage;

public interface IOrderMessagePublisher {

    void placeOrder(OrderMessage orderMessage);
}
