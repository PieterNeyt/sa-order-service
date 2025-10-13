package be.kdg.sa.backend.application;


import be.kdg.sa.backend.domain.*;
import be.kdg.sa.backend.domain.restaurant.AllRestaurant;
import be.kdg.sa.backend.domain.restaurant.Restaurant;
import be.kdg.sa.backend.domain.restaurant.RestaurantCatalog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantCatalog restaurantCatalog;

    public OrderService(OrderRepository orderRepository, RestaurantCatalog restaurantCatalog) {
        this.orderRepository = orderRepository;
        this.restaurantCatalog = restaurantCatalog;
    }

    public Order addDishToShoppingCart(UUID orderId, UUID dishId, int quantity,UUID clientId,String name,BigDecimal price,UUID restaurantId, int preparationTime) {
        RestaurantId restaurantID = new RestaurantId(restaurantId);

        Order order = this.orderRepository.findById(orderId)
                .orElseGet(() -> new Order(restaurantID,new ClientId(clientId)));

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

        order.place();

        this.orderRepository.save(order);
        return order;
    }


    public List<AllRestaurant> getRestaurants() {
        return restaurantCatalog.getRestaurants()
                .orElseThrow(() -> new RuntimeException("Restaurant niet gevonden"));
    }

    public Restaurant getRestaurantWithDishes(UUID restaurantId) {
        return restaurantCatalog.getRestaurantById(restaurantId).orElseThrow();
    }
}
