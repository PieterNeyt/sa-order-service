package be.kdg.sa.backend.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record RestaurantId(UUID id) {
    public RestaurantId {
        Assert.notNull(id, "id must not be null");
    }
}
