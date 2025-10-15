package be.kdg.sa.backend.infrastructure.restaurantcatalog;

import be.kdg.sa.backend.domain.order.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record CheckoutDto(
                          UUID orderId,
                          UUID restaurantId,
                          UUID clientId,
                          List<OrderLineDto> items
) {
    public static CheckoutDto fromOrderDomain(Order order) {
        List<OrderLineDto> items = order.getShoppingCart().stream()
                .map(line -> new OrderLineDto(
                        line.getDishId().id(),
                        line.getName(),
                        line.getPrice(),
                        line.getQuantity(),
                        line.getPreparationTime()
                ))
                .collect(Collectors.toList());
        return new CheckoutDto(order.getOrderId().id(),
                order.getRestaurantId().id(),
                order.getClientId().id(),
                items);
    }

    public record OrderLineDto(
            UUID dishId,
            String name,
            BigDecimal price,
            int quantity,
            int preparationTime
    ) {
    }
}
