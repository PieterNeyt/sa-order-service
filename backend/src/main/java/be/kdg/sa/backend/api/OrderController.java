package be.kdg.sa.backend.api;


import be.kdg.sa.backend.application.OrderService;
import be.kdg.sa.backend.domain.*;
import be.kdg.sa.backend.domain.restaurant.AllRestaurant;
import be.kdg.sa.backend.domain.restaurant.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:9090"})
public class OrderController {
    private final OrderService orderService;
    private final UUID CLIENT = UUID.randomUUID();

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{orderId}/shoppingCart/{restaurantId}")
    public ResponseEntity<OrderDto> addDishToShoppingCart(@PathVariable("orderId") UUID orderId,
                                                          @PathVariable("restaurantId") UUID restaurantId,
                                                          @RequestBody OrderDto.OrderLineDto orderDto) {
        Order order = orderService.addDishToShoppingCart(
                orderId,
                orderDto.dishId(),
                orderDto.quantity(),
                CLIENT,
                orderDto.name(),
                orderDto.price(),
                restaurantId,
                orderDto.preparationTime());
        return ResponseEntity.ok(OrderDto.from(order));
    }

    @GetMapping("/{orderId}/shoppingCart/")
    public ResponseEntity<List<OrderDto.OrderLineDto>> getShoppingCart(@PathVariable("orderId") UUID orderId) {
        Order order = orderService.getShoppingCart(orderId);
        return ResponseEntity.ok(OrderDto.from(order).shoppingCart());
    }

    @PatchMapping("/{orderId}/placeOrder")
    public ResponseEntity<List<OrderDto.OrderLineDto>> placeOrder(@PathVariable UUID orderId) {
        Order order = orderService.placeOrder(orderId);
        return ResponseEntity.ok(OrderDto.from(order).shoppingCart());
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<GetAllRestaurantDto>> getRestaurants() {
       List<AllRestaurant> restaurants = orderService.getRestaurants();
       return ResponseEntity.ok(restaurants.stream().map(GetAllRestaurantDto::from).toList());
    }
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantWithDishes(@PathVariable("id") UUID restaurantId) {
        Restaurant restaurant = orderService.getRestaurantWithDishes(restaurantId);
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }




}
