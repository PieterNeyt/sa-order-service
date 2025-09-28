package be.kdg.sa.backend.application;


import be.kdg.sa.backend.domain.DishId;
import be.kdg.sa.backend.domain.OrderId;
import be.kdg.sa.backend.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addDishToShoppingCart(OrderId orderId, DishId dishId, int quantity) {
    }
}
