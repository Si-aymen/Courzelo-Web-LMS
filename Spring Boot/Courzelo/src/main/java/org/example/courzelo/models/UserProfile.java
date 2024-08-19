package org.example.courzelo.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UserProfile {
    private String name;
    private String lastname;
    private String profileImage;
    private Date birthDate;
    private String gender;
    private String country;
    private String title;
    private String bio;

    private List<String> skills = new ArrayList<>();}
