package be.kdg.sa.backend.domain.order;


import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(OrderId orderId);

}
