package be.kdg.sa.backend.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record OrderId(UUID id) {
    public OrderId {
        Assert.notNull(id, "id must not be null");
    }
    public static OrderId create() {
        return new OrderId(UUID.randomUUID());
    }
}
