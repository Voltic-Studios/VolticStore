package dev.voltic.volticstore.request;

import lombok.Data;

@Data
public class ConfigResponse {
    private String publishableKey;

    public ConfigResponse(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    // Getter
}
