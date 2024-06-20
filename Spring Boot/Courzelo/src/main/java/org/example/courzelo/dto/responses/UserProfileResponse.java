package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.UserProfile;

import java.util.Date;

@Data
public class UserProfileResponse {
    private String name;
    private String lastname;
    private Date birthDate;
    private String title;
    private String bio;

    public UserProfileResponse(UserProfile userProfile) {
        this.name = userProfile.getName();
        this.lastname = userProfile.getLastname();
        this.birthDate = userProfile.getBirthDate();
        this.title = userProfile.getTitle();
        this.bio = userProfile.getBio();
    }
}
