package be.kdg.sa.backend.domain.order.orderline;

import org.springframework.util.Assert;

import java.util.UUID;

public record DishId(UUID id) {
    public DishId {
        Assert.notNull(id, "id must not be null");
    }
}
