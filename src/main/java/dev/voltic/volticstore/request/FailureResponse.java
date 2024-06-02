package dev.voltic.volticstore.request;

import lombok.Data;

import java.util.HashMap;

@Data
public class FailureResponse {
    private HashMap<String, String> error;

    public FailureResponse(String message) {
        this.error = new HashMap<>();
        this.error.put("message", message);
    }
}
