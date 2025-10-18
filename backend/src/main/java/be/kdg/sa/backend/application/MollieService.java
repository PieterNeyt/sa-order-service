package be.kdg.sa.backend.application;


import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.exception.MollieException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
@RequiredArgsConstructor
public class MollieService {

    private final Client mollieClient;

    @Value("${mollie.redirect.url}")
    private String redirectUrl;

    public boolean simulatePayment(BigDecimal amount, String description) {
        try {
            Amount paymentAmount = Amount.builder()
                    .currency("EUR")
                    .value(amount)
                    .build();

            PaymentRequest request = PaymentRequest.builder()
                    .amount(paymentAmount)
                    .description(description)
                    .redirectUrl(redirectUrl)
                    .build();

            mollieClient.payments().createPayment(request);


            // De payment is aangemaakt in mollie maar in test-modus blijft deze op open staan, dus we geven true terug als in "betaald"
            return true;

        } catch (Exception e) {
            return false; // betaling mislikt en dus ook niet aangemaakt in mollie
        }
    }
}
