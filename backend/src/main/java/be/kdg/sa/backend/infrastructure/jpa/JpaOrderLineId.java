package be.kdg.sa.backend.infrastructure.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
public class JpaOrderLineId {
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "dish_id")
    private UUID dishId;

    protected JpaOrderLineId() {}

    public JpaOrderLineId(UUID orderId, UUID productId) {
        this.orderId = orderId;
        this.dishId = productId;
    }
}
