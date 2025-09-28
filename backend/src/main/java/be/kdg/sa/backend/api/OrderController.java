package be.kdg.sa.backend.api;


import be.kdg.sa.backend.application.OrderService;
import be.kdg.sa.backend.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("addDish/{id}/")
    public ResponseEntity<Void> addDishToShoppingCart(@PathVariable("id") UUID id, @RequestBody OrderDto.OrderLineDto orderDto) {
        final OrderId orderId = new OrderId(id);
        final DishId dishId = new DishId(orderDto.dishId());
        orderService.addDishToShoppingCart(orderId,dishId,orderDto.quantity());
        return ResponseEntity.ok().build();
    }
}
