package be.kdg.sa.backend.infrastructure.handler;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderMessage(UUID id,
                           UUID restaurantId,
                           BigDecimal totalPrice) {
}

