package org.acme.implemenations;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.enums.PaymentMode;
import org.acme.interfaces.PaymentService;
import org.acme.annotations.PaymentType;

@ApplicationScoped
@PaymentType(PaymentMode.CREDIT_CARD)
public class CreditCardPayment implements PaymentService {

    @Override
    public void process() {
        Log.info("Payment mode is credit card");
    }
}
