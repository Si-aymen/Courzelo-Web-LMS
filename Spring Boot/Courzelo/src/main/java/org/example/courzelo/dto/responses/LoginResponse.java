package org.example.courzelo.dto.responses;

import lombok.Data;

@Data
public class LoginResponse {
    String status;
    String message;
    Boolean twoFactorAuth;
    UserResponse user;

    public LoginResponse(String status, String message, UserResponse user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public LoginResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public LoginResponse(String status, String message, Boolean twoFactorAuth, UserResponse user) {
        this.status = status;
        this.message = message;
        this.twoFactorAuth = twoFactorAuth;
        this.user = user;
    }

    public LoginResponse(String status, String message, Boolean twoFactorAuth) {
        this.status = status;
        this.message = message;
        this.twoFactorAuth = twoFactorAuth;
    }
}
