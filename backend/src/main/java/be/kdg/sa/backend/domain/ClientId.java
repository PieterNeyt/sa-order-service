package be.kdg.sa.backend.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record ClientId(UUID id) {
    public ClientId {
        Assert.notNull(id, "id must not be null");
    }
    public static ClientId create() {
        return new ClientId(UUID.randomUUID());
    }
}
