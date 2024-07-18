package org.example.courzelo.dto.requests;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String password;
    private String newPassword;
}
