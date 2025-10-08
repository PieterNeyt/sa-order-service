package be.kdg.sa.backend.application;


import be.kdg.sa.backend.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order addDishToShoppingCart(UUID orderId, UUID dishId, int quantity,UUID clientId,String name,BigDecimal price,UUID restaurantId, int preparationTime) {
        RestaurantId restaurantID = new RestaurantId(restaurantId);
        //check of al een order bestaat
        Order order = this.orderRepository.findById(orderId)
                .orElseGet(() -> new Order(restaurantID,new ClientId(clientId)));
        //toevoegen aan order
        order.addDish(
                new DishId(dishId),
                restaurantID,
                price,
                quantity,
                name,
                preparationTime
        );

        orderRepository.save(order);
        return order;

    }

    public Order getShoppingCart(UUID orderId) {
        return this.orderRepository.findById(orderId)
                .orElseThrow();
    }
    public Order placeOrder(UUID orderId) {
        var order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order niet gevonden"));

        // FIX: Gebruik een setter methode in plaats van directe assignment
        order.setOrderState(OrderState.PLACED);

        this.orderRepository.save(order);
        return order;
    }



}
