package be.kdg.sa.backend.api.dto;

import be.kdg.sa.backend.domain.order.Order;
import be.kdg.sa.backend.domain.order.OrderState;
import be.kdg.sa.backend.domain.order.orderline.OrderLine;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record OrderDto(
        UUID orderId,
        UUID clientId,
        UUID restaurantId,
        String orderState,
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
                translateOrderState(order.getOrderState(),order.getRejectionMessage()),
                lineDtos
        );
    }

    private static String translateOrderState(OrderState state,String message) {
        return switch (state) {
            case NOT_PLACED -> "Nog niet geplaatst";
            case PLACED -> "Geplaatst";
            case ACCEPTED -> "Bestelling geaccepteerd";
            case DENIED -> "Bestelling niet geaccepteerd voor volgende reden:"+message;
            case PICKED_UP -> "bestelling is opgehaald";
            case READY_FOR_PICKUP -> "Bestelling is klaar voor pickup";
            case DELIVERD -> "bestelling is aangekomen";
        };
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