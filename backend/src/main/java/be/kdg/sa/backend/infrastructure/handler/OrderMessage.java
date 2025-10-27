package be.kdg.sa.backend.infrastructure.handler;

import be.kdg.sa.backend.domain.order.orderline.OrderLine;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderMessage(UUID id,
                           UUID restaurantId,
                           BigDecimal totalPrice,
                           List<DishMessage> dishes) {


    public record DishMessage(UUID dishId, int amount) {
        public static List<DishMessage> fromDomain(List<OrderLine> shoppingCart) {
            return shoppingCart.stream().map(d -> new DishMessage(d.getDishId().id(), d.getQuantity())).toList();
        }
    }
}

