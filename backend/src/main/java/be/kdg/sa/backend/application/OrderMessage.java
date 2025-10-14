package be.kdg.sa.backend.application;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


public record OrderMessage(UUID id,
                           UUID restaurantId,
                           BigDecimal totalPrice) {
}
