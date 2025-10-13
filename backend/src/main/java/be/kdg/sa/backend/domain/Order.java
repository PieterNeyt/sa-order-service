package be.kdg.sa.backend.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@ToString
public class Order {
    private final OrderId orderId;
    private final ClientId clientId;
    private final RestaurantId restaurantId;
    private OrderState orderState;
    private final List<OrderLine> shoppingCart = new ArrayList<>();


    public Order(RestaurantId restaurantId, ClientId clientId) {
        Assert.notNull(restaurantId, "restaurantId must not be null");
        this.restaurantId = restaurantId;
        this.orderId = OrderId.create();
        this.clientId = clientId;
        this.orderState = OrderState.NOT_PLACED;
    }

    public Order(OrderId orderId, RestaurantId restaurantId, ClientId clientId, OrderState orderState) {
        this.orderState = orderState;
        Assert.notNull(restaurantId, "restaurantId must not be null");
        this.restaurantId = restaurantId;
        this.orderId = orderId;
        this.clientId = clientId;
    }


    public void addDish(final DishId dishId, final RestaurantId restaurantId, final BigDecimal price, int quantity,String name, int preparationTime) {
        Assert.isTrue(this.restaurantId.id().equals(restaurantId.id()), "All items in shopping cart must be from same restaurant");

        final var existingShoppingCart = shoppingCart.stream()
                .filter(sc -> sc.isForDishWithPrice(dishId,price))
                .findFirst();

        existingShoppingCart.ifPresentOrElse(
                shoppingCart -> shoppingCart.addQuantity(quantity),
                () -> shoppingCart.add(new OrderLine(dishId,price,quantity,name,preparationTime))
        );
    }


    public void place() {
        this.orderState=OrderState.PLACED;
    }
}
