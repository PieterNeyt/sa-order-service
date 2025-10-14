package be.kdg.sa.backend.api;


import be.kdg.sa.backend.application.OrderService;
import be.kdg.sa.backend.domain.*;
import be.kdg.sa.backend.domain.restaurant.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:9090"})
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private UUID getOwnerIdFromToken(@AuthenticationPrincipal Jwt token) {
        return UUID.fromString(token.getClaimAsString("sub"));
    }

    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/{orderId}/shoppingCart/{restaurantId}")
    public ResponseEntity<OrderDto> addDishToShoppingCart(@PathVariable("orderId") UUID orderId,
                                                          @PathVariable("restaurantId") UUID restaurantId,
                                                          @RequestBody OrderDto.OrderLineDto orderDto,
                                                          @AuthenticationPrincipal Jwt token) {
        Order order = orderService.addDishToShoppingCart(
                orderId,
                orderDto.dishId(),
                orderDto.quantity(),
                getOwnerIdFromToken(token),
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
    @PreAuthorize("hasAuthority('client')")
    @PatchMapping("/{orderId}/placeOrder")
    public ResponseEntity<List<OrderDto.OrderLineDto>> placeOrder(@PathVariable UUID orderId) {
        Order order = orderService.placeOrder(orderId);
        return ResponseEntity.ok(OrderDto.from(order).shoppingCart());
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<GetAllRestaurantDto>> getRestaurants() {
       List<Restaurant> restaurants = orderService.getRestaurants();
       return ResponseEntity.ok(restaurants.stream().map(GetAllRestaurantDto::from).toList());
    }
    @GetMapping("{restaurantId}/dished")
    public ResponseEntity<List<GetAllRestaurantDto>> getDishes(@PathVariable("restaurantId") UUID restaurantId) {
        List<Restaurant> restaurants = orderService.getRestaurants();
        return ResponseEntity.ok(restaurants.stream().map(GetAllRestaurantDto::from).toList());
    }




}
