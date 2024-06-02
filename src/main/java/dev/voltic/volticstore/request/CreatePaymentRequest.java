package dev.voltic.volticstore.request;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CreatePaymentRequest {
    @SerializedName("paymentMethodType")
    private String paymentMethodType;

    @SerializedName("currency")
    private String currency;

}

