package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UserProfile {
    private String name;
    private String lastname;
    private String profileImage;
    private Date birthDate;
    private String title;
    private String bio;
}
