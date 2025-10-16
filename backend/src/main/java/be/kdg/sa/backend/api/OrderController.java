package be.kdg.sa.backend.api;


import be.kdg.sa.backend.api.dto.*;
import be.kdg.sa.backend.application.OrderService;
import be.kdg.sa.backend.domain.order.Order;
import be.kdg.sa.backend.domain.order.OrderId;
import be.kdg.sa.backend.domain.order.RestaurantId;
import be.kdg.sa.backend.domain.order.orderline.DishId;
import be.kdg.sa.backend.domain.restaurant.AllRestaurant;
import be.kdg.sa.backend.domain.restaurant.Restaurant;
import be.kdg.sa.backend.infrastructure.restaurantcatalog.CheckoutDto;
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

    private UUID getIdFromToken(@AuthenticationPrincipal Jwt token) {
        return UUID.fromString(token.getClaimAsString("sub"));
    }

    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/{orderId}/shoppingCart/{restaurantId}")
    public ResponseEntity<OrderDto> addDishToShoppingCart(@PathVariable("orderId") UUID orderId,
                                                          @PathVariable("restaurantId") UUID restaurantId,
                                                          @RequestBody OrderDto.OrderLineDto orderDto,
                                                          @AuthenticationPrincipal Jwt token) {
        Order order = orderService.addDishToShoppingCart(
                new OrderId(orderId),
                new DishId(orderDto.dishId()),
                new RestaurantId(restaurantId),
                orderDto.quantity(),
                getIdFromToken(token),
                orderDto.name(),
                orderDto.price(),
                orderDto.preparationTime());

        return ResponseEntity.ok(OrderDto.from(order));
    }

    @PostMapping("/prepareCheckout")
    public ResponseEntity<CheckoutResponseDto> prepareCheckout(@RequestBody CheckoutDto checkoutDto) {
        CheckoutResponseDto response = orderService.prepareCheckout(checkoutDto);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") UUID orderId) {
        Order order = orderService.getOrderById(new OrderId(orderId));
        return ResponseEntity.ok(OrderDto.from(order));
    }

    @GetMapping("/{orderId}/shoppingCart/")
    public ResponseEntity<List<OrderDto.OrderLineDto>> getShoppingCart(@PathVariable("orderId") UUID orderId) {
        Order order = orderService.getShoppingCart(new OrderId(orderId));
        return ResponseEntity.ok(OrderDto.from(order).shoppingCart());
    }

    @PreAuthorize("hasAuthority('client')")
    @PatchMapping("/{orderId}/placeOrder")
    public ResponseEntity<List<OrderDto.OrderLineDto>> placeOrder(
            @PathVariable UUID orderId,
            @RequestBody OrderInformationDto orderInformation,
            @AuthenticationPrincipal Jwt token) {

        UUID clientId = getIdFromToken(token);
        Order order = orderService.placeOrder(new OrderId(orderId), orderInformation, clientId);
        return ResponseEntity.ok(OrderDto.from(order).shoppingCart());
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<GetAllRestaurantDto>> getRestaurants() {
       List<AllRestaurant> restaurants = orderService.getRestaurants();
       return ResponseEntity.ok(restaurants.stream().map(GetAllRestaurantDto::from).toList());
    }
    @GetMapping("/{restaurantId}/dishes")
    public ResponseEntity<RestaurantDto> getDishes(@PathVariable("restaurantId") UUID restaurantId) {
        Restaurant restaurant = orderService.getRestaurantWithDishes(new RestaurantId(restaurantId));
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantWithDishes(@PathVariable("id") UUID restaurantId) {
        Restaurant restaurant = orderService.getRestaurantWithDishes(new RestaurantId(restaurantId));
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }
    @GetMapping("/dish/{id}")
    public ResponseEntity<RestaurantDto.DishDto> getDishById(@PathVariable("id") UUID dishId) {
        Restaurant.Dish dish = orderService.getDishById(new DishId(dishId));
        return ResponseEntity.ok(RestaurantDto.DishDto.from(dish));
    }




}
