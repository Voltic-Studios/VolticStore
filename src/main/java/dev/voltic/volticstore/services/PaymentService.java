package dev.voltic.volticstore.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import dev.voltic.volticstore.request.CreatePaymentRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public PaymentIntent createPaymentIntent(CreatePaymentRequest createPaymentRequest) throws StripeException {
        PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams
                .builder()
                .addPaymentMethodType(createPaymentRequest.getPaymentMethodType())
                .setCurrency(createPaymentRequest.getCurrency())
                .setAmount(5999L);

        if (createPaymentRequest.getPaymentMethodType().equals("link")) {
            paramsBuilder.addPaymentMethodType("card").addPaymentMethodType("link");
        }

        if (createPaymentRequest.getPaymentMethodType().equals("acss_debit")) {
            paramsBuilder.setPaymentMethodOptions(
                    PaymentIntentCreateParams.PaymentMethodOptions.builder()
                            .setAcssDebit(
                                    PaymentIntentCreateParams.PaymentMethodOptions.AcssDebit.builder()
                                            .setMandateOptions(
                                                    PaymentIntentCreateParams.PaymentMethodOptions.AcssDebit.MandateOptions.builder()
                                                            .setPaymentSchedule(PaymentIntentCreateParams.PaymentMethodOptions.AcssDebit.MandateOptions.PaymentSchedule.SPORADIC)
                                                            .setTransactionType(PaymentIntentCreateParams.PaymentMethodOptions.AcssDebit.MandateOptions.TransactionType.PERSONAL)
                                                            .build())
                                            .build())
                            .build());
        }

        return PaymentIntent.create(paramsBuilder.build());
    }
}
