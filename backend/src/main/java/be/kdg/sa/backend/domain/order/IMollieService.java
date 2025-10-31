package be.kdg.sa.backend.domain.order;

import be.kdg.sa.backend.api.dto.PaymentCreationDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface IMollieService {
    PaymentCreationDto createPayment(BigDecimal amount, String description, UUID orderId);
    boolean verifyPayment(String paymentId);
}
