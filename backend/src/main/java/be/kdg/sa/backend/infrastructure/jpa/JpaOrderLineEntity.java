package be.kdg.sa.backend.infrastructure.jpa;

import be.kdg.sa.backend.domain.OrderLine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="orderLine")
public class JpaOrderLineEntity {
    @EmbeddedId
    private JpaOrderLineId id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private JpaOrderEntity order;

    public JpaOrderLineEntity() {}

    public JpaOrderLineEntity(UUID orderId,UUID dishId, BigDecimal price, int quantity) {
        this.id = new JpaOrderLineId(orderId, dishId);
        this.price = price;
        this.quantity = quantity;
    }

    public static JpaOrderLineEntity fromDomain(OrderLine orderLine, UUID orderId) {
        return new JpaOrderLineEntity(
                orderId,
                orderLine.getDishId().id(),
                orderLine.getPrice(),
                orderLine.getQuantity()
        );
    }
}
