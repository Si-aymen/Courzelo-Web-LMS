package org.example.courzelo.dto.requests;

import lombok.Data;

import java.util.Date;

@Data
public class ProfileInformationRequest {
    String name;
    String lastname;
    String bio;
    String title;
    String birthDate;


}
