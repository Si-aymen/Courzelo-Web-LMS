package org.example.courzelo.dto.requests;

import lombok.Data;

import java.util.Date;

@Data
public class SignupRequest {
    private String email;
    private String name;
    private String lastname;
    private Date birthDate;
    private String gender;
    private String country;
    private String password;
}
