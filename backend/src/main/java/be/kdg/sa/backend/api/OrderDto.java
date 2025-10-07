package be.kdg.sa.backend.api;

import be.kdg.sa.backend.domain.Order;
import be.kdg.sa.backend.domain.OrderLine;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record OrderDto(
        UUID orderId,
        UUID clientId,
        UUID restaurantId,
        List<OrderLineDto> shoppingCart
) {
    public static OrderDto from(final Order order) {
        List<OrderLineDto> lineDtos = order.getShoppingCart().stream()
                .map(OrderLineDto::from)
                .collect(Collectors.toList());
        return new OrderDto(
                order.getOrderId().id(),
                order.getClientId().id(),
                order.getRestaurantId().id(),
                lineDtos
        );
    }

    public record OrderLineDto(
            UUID dishId,
            BigDecimal price,
            int quantity,
            String name,
            int preparationTime
    ) {
        public static OrderLineDto from(final OrderLine orderLine) {
            return new OrderLineDto(
                    orderLine.getDishId().id(),
                    orderLine.getPrice(),
                    orderLine.getQuantity(),
                    orderLine.getName(),
                    orderLine.getPreparationTime()
            );
        }

    }
}
