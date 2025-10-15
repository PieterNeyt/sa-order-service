package be.kdg.sa.backend.infrastructure.handler;

import java.util.UUID;

public record RestaurantResponse(UUID orderId,
                                 String isAccepted,
                                 String message) {
}
