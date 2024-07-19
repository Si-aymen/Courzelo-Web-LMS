package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserProfile;

import java.util.List;

@Data
public class UserResponse {
    String email;
    List<String> roles;
    UserProfileResponse profile;
    UserSecurityResponse security;


    public UserResponse(User user) {
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Enum::name).toList();
        this.profile = new UserProfileResponse(user.getProfile() != null ? user.getProfile() : new UserProfile());
        this.security = new UserSecurityResponse(user.getSecurity());
    }

}
