package be.kdg.sa.backend.api;


import be.kdg.sa.backend.application.OrderService;
import be.kdg.sa.backend.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final UUID CLIENT = UUID.randomUUID();

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{orderId}/shoppingCart/")
    public ResponseEntity<OrderDto> addDishToShoppingCart(@PathVariable("orderId") UUID orderId, @RequestBody OrderDto.OrderLineDto orderDto) {
        Order order = orderService.addDishToShoppingCart(orderId,orderDto.dishId(),orderDto.quantity(),CLIENT,orderDto.name());
        return ResponseEntity.ok(OrderDto.from(order));
    }
    @GetMapping("/{orderId}/shoppingCart/")
    public ResponseEntity<List<OrderDto.OrderLineDto>> getShoppingCart(@PathVariable("orderId") UUID orderId) {
        Order order = orderService.getShoppingCart(orderId);
        return ResponseEntity.ok(OrderDto.from(order).shoppingCart());
    }
}
