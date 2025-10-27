package be.kdg.sa.backend.application;


import be.kdg.sa.backend.api.dto.CheckoutResponseDto;
import be.kdg.sa.backend.api.dto.OrderInformationDto;
import be.kdg.sa.backend.domain.NotFoundException;
import be.kdg.sa.backend.domain.client.ClientId;
import be.kdg.sa.backend.domain.order.*;
import be.kdg.sa.backend.domain.order.orderline.DishId;
import be.kdg.sa.backend.domain.restaurant.AllRestaurant;
import be.kdg.sa.backend.domain.restaurant.Restaurant;
import be.kdg.sa.backend.domain.restaurant.RestaurantCatalog;
import be.kdg.sa.backend.infrastructure.config.RabbitMQTopology;
import be.kdg.sa.backend.infrastructure.handler.OrderMessage;
import be.kdg.sa.backend.infrastructure.handler.RestaurantResponse;
import be.kdg.sa.backend.infrastructure.restaurantcatalog.CheckoutDto;
import lombok.extern.slf4j.Slf4j;
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
    private final ClientService clientService;
    private final MollieService mollieService;
    private final IOrderMessagePublisher orderMessageService;

    public OrderService(OrderRepository orderRepository, RestaurantCatalog restaurantCatalog, ClientService clientService, MollieService mollieService, IOrderMessagePublisher orderMessageService) {
        this.orderRepository = orderRepository;
        this.restaurantCatalog = restaurantCatalog;
        this.clientService = clientService;
        this.mollieService = mollieService;
        this.orderMessageService = orderMessageService;
    }

    public Order getOrderById(OrderId orderId) {
        return this.orderRepository.findById(orderId)
                .orElseThrow(orderId::notFound);
    }

    public Order addDishToShoppingCart(
            OrderId orderId,
            DishId dishId,
            RestaurantId restaurantId,
            int quantity,
            UUID clientId,
            String name,
            BigDecimal price,
            int preparationTime) {

        final Order order = this.orderRepository.findById(orderId)
                .orElseGet(() -> new Order(restaurantId, new ClientId(clientId)));

        order.addDish(
                dishId,
                restaurantId,
                price,
                quantity,
                name,
                preparationTime
        );

        orderRepository.save(order);
        return order;
    }

    public Order getShoppingCart(OrderId orderId) {
        return this.orderRepository.findById(orderId)
                .orElseThrow(orderId::notFound);
    }

    public Order placeOrder(OrderId orderId, OrderInformationDto orderInformation, UUID clientId) {

        final Order order = this.orderRepository.findById(orderId)
                .orElseThrow(orderId::notFound);

        restaurantCatalog.checkShoppingCart(
                CheckoutDto.fromOrderDomain(order)
        );

        clientService.saveOrUpdateClient(clientId, orderInformation);

        BigDecimal totalPrice = order.calculateTotalPrice();

        boolean paymentSuccess = mollieService.simulatePayment(
                totalPrice,
                "Bestelling bij restaurant " + order.getRestaurantId().id()
        );

        if (!paymentSuccess) {
            throw new RuntimeException("Betaling mislukt");
        }
        order.place();

        this.orderRepository.save(order);
        this.orderMessageService.placeOrder(
                new OrderMessage(
                        order.getOrderId().id(),
                        order.getRestaurantId().id(),
                        order.calculateTotalPrice(),
                        OrderMessage.DishMessage.fromDomain(order.getShoppingCart())
                ));
        return order;
    }


    public List<AllRestaurant> getRestaurants() {
        return restaurantCatalog.getRestaurants()
                .orElseThrow(() -> new NotFoundException("Restaurants niet gevonden"));
    }

    public Restaurant getRestaurantWithDishes(RestaurantId restaurantId) {
        return restaurantCatalog.getRestaurantById(restaurantId).
                orElseThrow(restaurantId::notFound);
    }

    public Restaurant.Dish getDishById(DishId dishId) {
        return restaurantCatalog.getDishById(dishId)
                .orElseThrow(dishId::notFound);
    }

    public CheckoutResponseDto prepareCheckout(CheckoutDto checkoutDto) {
        return restaurantCatalog.checkShoppingCart(checkoutDto);
    }

    public void orderAccepted(RestaurantResponse msg) {
        final OrderId orderId = new OrderId(msg.orderId());

        final Order order = orderRepository.findById(orderId)
                .orElseThrow(orderId::notFound);

        order.accept();
        orderRepository.save(order);
    }

    public void orderDenied(RestaurantResponse msg) {
        final OrderId orderId = new OrderId(msg.orderId());

        final Order order = orderRepository.findById(orderId)
                .orElseThrow(orderId::notFound);

        order.denied(msg.message());
        orderRepository.save(order);
    }

    public void orderReady(RestaurantResponse msg) {
        final OrderId orderId = new OrderId(msg.orderId());

        final Order order = orderRepository.findById(orderId)
                .orElseThrow(orderId::notFound);

        order.ready();
        orderRepository.save(order);
    }

    public void orderPickedUp(RestaurantResponse msg) {
        final OrderId orderId = new OrderId(msg.orderId());

        final Order order = orderRepository.findById(orderId)
                .orElseThrow(orderId::notFound);

        order.pickedUp();
        orderRepository.save(order);
    }

    public void orderDeliverd(RestaurantResponse msg) {
        final OrderId orderId = new OrderId(msg.orderId());

        final Order order = orderRepository.findById(orderId)
                .orElseThrow(orderId::notFound);

        order.deliverd();
        orderRepository.save(order);
    }

    public void orderClaimed(RestaurantResponse msg) {
        final OrderId orderId = new OrderId(msg.orderId());

        final Order order = orderRepository.findById(orderId)
                .orElseThrow(orderId::notFound);

        order.claimed();
        orderRepository.save(order);
    }
}
