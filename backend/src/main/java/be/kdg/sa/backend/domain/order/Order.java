package be.kdg.sa.backend.domain.order;

import be.kdg.sa.backend.domain.ActionNotPossibleException;
import be.kdg.sa.backend.domain.client.ClientId;
import be.kdg.sa.backend.domain.order.orderline.DishId;
import be.kdg.sa.backend.domain.order.orderline.OrderLine;
import be.kdg.sa.backend.infrastructure.handler.RestaurantResponse;
import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@AggregateRoot
@ToString
public class Order {
    private final OrderId orderId;
    private final ClientId clientId;
    private final RestaurantId restaurantId;
    private OrderState orderState;
    private String rejectionMessage;
    private final List<OrderLine> shoppingCart = new ArrayList<>();


    public Order(RestaurantId restaurantId, ClientId clientId) {
        Assert.notNull(restaurantId, "restaurantId must not be null");
        this.restaurantId = restaurantId;
        this.orderId = OrderId.create();
        this.clientId = clientId;
        this.orderState = OrderState.NOT_PLACED;
    }

    public Order(OrderId orderId, RestaurantId restaurantId, ClientId clientId, OrderState orderState,String rejectionMessage) {
        this.orderState = orderState;
        Assert.notNull(restaurantId, "restaurantId must not be null");
        this.restaurantId = restaurantId;
        this.orderId = orderId;
        this.clientId = clientId;
        this.rejectionMessage = rejectionMessage;
    }


    public void addDish(final DishId dishId, final RestaurantId restaurantId, final BigDecimal price, int quantity, String name, int preparationTime) {
        if(!this.restaurantId.id().equals(restaurantId.id()))
            throw new ActionNotPossibleException("All items in shopping cart must be from same restaurant");

        final var existingShoppingCart = shoppingCart.stream()
                .filter(sc -> sc.isForDishWithPrice(dishId,price))
                .findFirst();

        existingShoppingCart.ifPresentOrElse(
                shoppingCart -> shoppingCart.addQuantity(quantity),
                () -> shoppingCart.add(new OrderLine(dishId,price,quantity,name,preparationTime))
        );
    }


    public void place() {
        if(this.orderState != OrderState.NOT_PLACED)
            throw new  ActionNotPossibleException("Order cant be placed");

        this.orderState=OrderState.PLACED;
    }

    public BigDecimal calculateTotalPrice() {
        return shoppingCart.stream()
                .map(orderLine -> orderLine.getPrice().multiply(BigDecimal.valueOf(orderLine.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void accept() {
        if(this.orderState != OrderState.PLACED)
            throw new  ActionNotPossibleException("Order isnt placed anymore");

        this.orderState=OrderState.ACCEPTED;

    }

    public void ready() {
        if(this.orderState != OrderState.ACCEPTED)
            throw new  ActionNotPossibleException("Order isnt accepted/ready anymore");

        this.orderState=OrderState.READY_FOR_PICKUP;
    }

    public void pickedUp() {
        if(this.orderState != OrderState.READY_FOR_PICKUP)
            throw new  ActionNotPossibleException("Order isnt ready for pick up");

        this.orderState=OrderState.PICKED_UP;
    }

    public void deliverd() {
        if(this.orderState != OrderState.PICKED_UP)
            throw new  ActionNotPossibleException("Order isnt picked up");

        this.orderState=OrderState.DELIVERD;
    }
    public void claimed() {
        if(this.orderState != OrderState.ACCEPTED)
            throw new  ActionNotPossibleException("Order isnt accepted by restaurant yet");

        this.orderState=OrderState.DELIVERD;
    }

    public void denied(String msg) {
        if(this.orderState != OrderState.PLACED)
            throw new  ActionNotPossibleException("Order isnt placed anymore");

        rejectedMessage(msg);
        this.orderState=OrderState.DENIED;
    }
    public void rejectedMessage(String msg) {
        if(msg.isBlank())
            throw new ActionNotPossibleException("Rejection msg must not be empty");

        this.rejectionMessage = msg;
    }
}
