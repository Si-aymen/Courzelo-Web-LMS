package org.example.courzelo.dto.responses;

import lombok.Data;

@Data
public class StatusMessageResponse {
    private String status;
    private String message;

    public StatusMessageResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
