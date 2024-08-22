package org.example.courzelo.dto.requests;

import lombok.Data;

import java.util.Date;

@Data
public class UserProfileRequest {
    private String name;
    private String lastName;
    private String profileImage;
    private Date birthDate;
    private String title;
    private String bio;
}
