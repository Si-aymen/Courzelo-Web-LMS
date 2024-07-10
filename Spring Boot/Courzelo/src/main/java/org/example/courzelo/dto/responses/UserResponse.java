package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserProfile;

@Data
public class UserResponse {
    String email;
    UserProfileResponse profile;
    UserSecurityResponse security;


    public UserResponse(User user) {
        this.email = user.getEmail();
        this.profile = new UserProfileResponse(user.getProfile() != null ? user.getProfile() : new UserProfile());
        this.security = new UserSecurityResponse(user.getSecurity());
    }

}
