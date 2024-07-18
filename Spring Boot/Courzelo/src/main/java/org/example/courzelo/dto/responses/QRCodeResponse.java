package org.example.courzelo.dto.responses;

import lombok.Data;

@Data
public class QRCodeResponse {
    private String status;
    private String message;
    private String qrCodeImage;

    public QRCodeResponse(String status, String message, String qrCodeImage) {
        this.status = status;
        this.message = message;
        this.qrCodeImage = qrCodeImage;
    }
}
