package be.kdg.sa.backend.api.dto;

import java.util.UUID;


public record PaymentDto(String paymentUrl, UUID orderId) {}