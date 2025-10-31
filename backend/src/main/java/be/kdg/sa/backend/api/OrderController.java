package be.kdg.sa.backend.api;


import be.kdg.sa.backend.api.dto.*;
import be.kdg.sa.backend.application.ClientService;
import be.kdg.sa.backend.application.OrderService;
import be.kdg.sa.backend.domain.order.Order;
import be.kdg.sa.backend.domain.order.OrderId;
import be.kdg.sa.backend.domain.order.RestaurantId;
import be.kdg.sa.backend.domain.order.orderline.DishId;
import be.kdg.sa.backend.domain.restaurant.Restaurant;
import be.kdg.sa.backend.infrastructure.restaurantcatalog.CheckoutDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final ClientService clientService;

    public OrderController(OrderService orderService, ClientService clientService) {
        this.orderService = orderService;
        this.clientService = clientService;
    }

    @PostMapping("/{orderId}/shoppingCart/{restaurantId}")
    public ResponseEntity<OrderDto> addDishToShoppingCart(
            @PathVariable UUID orderId,
            @PathVariable UUID restaurantId,
            @RequestParam UUID clientId,
            @RequestBody OrderDto.OrderLineDto orderDto
    ) {
        Order order = orderService.addDishToShoppingCart(
                new OrderId(orderId),
                new DishId(orderDto.dishId()),
                new RestaurantId(restaurantId),
                orderDto.quantity(),
                clientId,
                orderDto.name(),
                orderDto.price(),
                orderDto.preparationTime()
        );

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

    @GetMapping("/login")
    public ResponseEntity<ClientDto> login() {
        UUID fixedUserId = UUID.fromString("b775ec99-08d9-49c8-9cc7-d0e5d57593bb");
        ClientDto clientDto = clientService.login(fixedUserId);
        return ResponseEntity.ok(clientDto);
    }


    @GetMapping("/{orderId}/shoppingCart/")
    public ResponseEntity<List<OrderDto.OrderLineDto>> getShoppingCart(@PathVariable("orderId") UUID orderId) {
        Order order = orderService.getShoppingCart(new OrderId(orderId));
        return ResponseEntity.ok(OrderDto.from(order).shoppingCart());
    }


    @PatchMapping("/{orderId}/placeOrder")
    public ResponseEntity<List<OrderDto.OrderLineDto>> placeOrder(
            @PathVariable UUID orderId,
            @RequestParam UUID clientId,
            @RequestBody OrderInformationDto orderInformation) {

        Order order = orderService.placeOrder(new OrderId(orderId), orderInformation, clientId);
        return ResponseEntity.ok(OrderDto.from(order).shoppingCart());
    }


    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDto>> getRestaurants() {
        List<Restaurant> restaurants = orderService.getRestaurants();
        return ResponseEntity.ok(restaurants.stream().map(RestaurantDto::from).toList());
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
