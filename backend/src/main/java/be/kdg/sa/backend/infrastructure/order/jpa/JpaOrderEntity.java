package be.kdg.sa.backend.infrastructure.order.jpa;

import be.kdg.sa.backend.domain.client.ClientId;
import be.kdg.sa.backend.domain.order.*;
import be.kdg.sa.backend.domain.order.orderline.DishId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="orders")
public class JpaOrderEntity {
    @Id
    private UUID orderId;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column()
    private String rejectionMessage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL ,fetch = FetchType.LAZY, orphanRemoval = true)
    private List<JpaOrderLineEntity> shoppingCart;

    public JpaOrderEntity() {}

    public JpaOrderEntity(UUID orderId, UUID clientId, UUID restaurantId,OrderState orderState,String rejectionMessage) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.restaurantId = restaurantId;
        this.shoppingCart = new ArrayList<>();
        this.orderState = orderState;
        this.rejectionMessage = rejectionMessage;
    }
    public static JpaOrderEntity fromDomain(Order order) {
        JpaOrderEntity orderEntity = new JpaOrderEntity(
                order.getOrderId().id(),
                order.getClientId().id(),
                order.getRestaurantId().id(),
                order.getOrderState(),
                order.getRejectionMessage()
        );
        List<JpaOrderLineEntity> orderLines = order.getShoppingCart().stream()
                .map(orderLine -> JpaOrderLineEntity.fromDomain(orderLine,order.getOrderId().id()))
                .toList();
        orderEntity.setLines(orderLines);
        return orderEntity;
    }
    public Order toDomain() {
        Order order = new Order(new OrderId(orderId),
                new RestaurantId(restaurantId),
                new ClientId(clientId),
                orderState,
                rejectionMessage
        );
        shoppingCart.forEach(orderLine -> order.addDish(
               new DishId(orderLine.getId().getDishId()),
                order.getRestaurantId(),
                orderLine.getPrice(),
                orderLine.getQuantity(),
                orderLine.getName(),
                orderLine.getPreparationTime()
        ));
        return order;
    }
    public void setLines(List<JpaOrderLineEntity> lines) {
        this.shoppingCart = lines;
        this.shoppingCart.forEach(line -> line.setOrder(this));
    }
}
