package dev.voltic.volticstore.services;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    private String stripeApiKey = "sk_test_51PL56ZA6mET3VAIuZ7xzL4jLYd87ZWngdbH4dM7IKwhiLqaS2RrDvILHOHRdZtIxzImIxqm0MuUCB4IwTllUUmka00PQN8egUN";

    private String stripePublicKey = "pk_test_51PL56ZA6mET3VAIuG91zU6NruZSyQaA9sIWqYBYPN0QJlCOjs9JYR5C82R8zLtCC1Vs65aFUfOKE14KvUu9Dqrz300fMo5m2Bt";

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public PaymentIntent createPaymentIntent(int amount, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) amount)
                .setCurrency(currency)
                .build();

        return PaymentIntent.create(params);
    }

    public Charge charge(String token, int amount) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", "usd");
        chargeParams.put("source", token); // use the token passed as parameter

        return Charge.create(chargeParams);
    }
}
