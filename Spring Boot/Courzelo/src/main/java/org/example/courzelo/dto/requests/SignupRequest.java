package org.example.courzelo.dto.requests;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
}
