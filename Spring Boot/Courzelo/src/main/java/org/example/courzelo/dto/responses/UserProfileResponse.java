package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.UserProfile;

import java.util.Date;
import java.util.List;

@Data
public class UserProfileResponse {
    private String name;
    private String lastName;
    private String profileImage;
    private Date birthDate;
    private String title;
    private String bio;
    private List<String> skills;

    public UserProfileResponse(UserProfile userProfile) {
        this.name = userProfile.getName();
        this.lastName = userProfile.getLastName();
        this.profileImage = userProfile.getProfileImage();
        this.birthDate = userProfile.getBirthDate();
        this.title = userProfile.getTitle();
        this.bio = userProfile.getBio();
    }
}
