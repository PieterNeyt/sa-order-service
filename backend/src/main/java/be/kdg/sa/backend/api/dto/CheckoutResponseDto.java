package be.kdg.sa.backend.api.dto;

import java.util.UUID;

public record CheckoutResponseDto(
        UUID orderId,
        boolean success,
        String message
) {
}