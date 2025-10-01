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
    private final UUID RESTAURANT_ID =  UUID.randomUUID();
    private final BigDecimal PRICE = BigDecimal.valueOf(12.45);


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order addDishToShoppingCart(UUID orderId, UUID dishId, int quantity,UUID clientId) {
        /*TODO : via Rabbit na kijken of de dish wel bestaat
            - krijg je confirmatie dat de dish bestaat
            - Prijs van de dish krijgen
            - Restaurant Id van krijgen
        */
        RestaurantId restaurantId = new RestaurantId(RESTAURANT_ID);
        //check of al een order bestaat
        Order order = this.orderRepository.findById(orderId)
                .orElseGet(() -> new Order(restaurantId,new ClientId(clientId)));
        //toevoegen aan order
        order.addDish(
                new DishId(dishId),
                restaurantId,
                PRICE,
                quantity
        );

        orderRepository.save(order);
        return order;

    }

    public Order getShoppingCart(UUID orderId) {
        return this.orderRepository.findById(orderId)
                .orElseThrow();
    }
}
