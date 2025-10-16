package be.kdg.sa.backend.domain.order;

import be.kdg.sa.backend.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record RestaurantId(UUID id) {
    public RestaurantId {
        Assert.notNull(id, "id must not be null");
    }
    public NotFoundException notFound() {
        return new NotFoundException("Order [" + id + "] not found");
    }
}
