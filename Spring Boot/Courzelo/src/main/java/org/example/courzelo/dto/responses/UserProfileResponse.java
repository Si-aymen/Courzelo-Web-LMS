package org.example.courzelo.dto.responses;

import lombok.Builder;
import lombok.Data;
import org.example.courzelo.models.UserProfile;

import java.util.Date;

@Data
public class UserProfileResponse {
    private String name;
    private String lastname;
    private Date birthDate;
    private String gender;
    private String country;
    private String title;
    private String bio;

    public UserProfileResponse(UserProfile userProfile) {
        this.name = userProfile.getName();
        this.lastname = userProfile.getLastname();
        this.birthDate = userProfile.getBirthDate();
        this.gender = userProfile.getGender();
        this.country = userProfile.getCountry();
        this.title = userProfile.getTitle();
        this.bio = userProfile.getBio();
    }
}
