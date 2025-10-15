package be.kdg.sa.backend.application;


import be.kdg.sa.backend.api.dto.CheckoutResponseDto;
import be.kdg.sa.backend.api.dto.OrderInformationDto;
import be.kdg.sa.backend.domain.client.ClientId;
import be.kdg.sa.backend.domain.order.orderline.DishId;
import be.kdg.sa.backend.domain.order.Order;
import be.kdg.sa.backend.domain.order.OrderRepository;
import be.kdg.sa.backend.domain.order.RestaurantId;
import be.kdg.sa.backend.domain.restaurant.AllRestaurant;
import be.kdg.sa.backend.domain.restaurant.Restaurant;
import be.kdg.sa.backend.domain.restaurant.RestaurantCatalog;
import be.kdg.sa.backend.infrastructure.config.RabbitMQTopology;
import be.kdg.sa.backend.infrastructure.restaurantcatalog.CheckoutDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantCatalog restaurantCatalog;
    private final RabbitTemplate rabbitTemplate;
    private final ClientService clientService;

    public OrderService(OrderRepository orderRepository, RestaurantCatalog restaurantCatalog, RabbitTemplate rabbitTemplate, ClientService clientService) {
        this.orderRepository = orderRepository;
        this.restaurantCatalog = restaurantCatalog;
        this.rabbitTemplate = rabbitTemplate;
        this.clientService = clientService;
    }

    public Order addDishToShoppingCart(UUID orderId, UUID dishId, int quantity, UUID clientId, String name, BigDecimal price, UUID restaurantId, int preparationTime) {
        RestaurantId restaurantID = new RestaurantId(restaurantId);

        Order order = this.orderRepository.findById(orderId)
                .orElseGet(() -> new Order(restaurantID, new ClientId(clientId)));

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

    public Order placeOrder(UUID orderId, OrderInformationDto orderInformation, UUID clientId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order niet gevonden"));

        restaurantCatalog.checkShoppingCart(
                CheckoutDto.fromOrderDomain(order)
        );


        clientService.saveOrUpdateClient(clientId, orderInformation);

        order.place();

        this.orderRepository.save(order);
        this.rabbitTemplate.convertAndSend(
                RabbitMQTopology.ORDER_EXCHANGE_NAME,
                "order.place." + order.getOrderId().id(),
                new OrderMessage(order.getOrderId().id(),
                        order.getRestaurantId().id(),
                        order.calculateTotalPrice())
        );

        return order;
    }


    public List<AllRestaurant> getRestaurants() {
        return restaurantCatalog.getRestaurants()
                .orElseThrow(() -> new RuntimeException("Restaurant niet gevonden"));
    }

    public Restaurant getRestaurantWithDishes(UUID restaurantId) {
        return restaurantCatalog.getRestaurantById(restaurantId).orElseThrow();
    }

    public Restaurant.Dish getDishById(UUID dishId) {
        return restaurantCatalog.getDishById(dishId).orElseThrow();
    }

    public CheckoutResponseDto prepareCheckout(CheckoutDto checkoutDto) {
        return restaurantCatalog.checkShoppingCart(checkoutDto);
    }
}
