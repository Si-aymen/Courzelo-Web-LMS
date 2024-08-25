package org.example.courzelo.dto.responses;

import lombok.Builder;
import lombok.Data;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserProfile;

import java.util.List;

@Data
@Builder
public class UserResponse {
    String email;
    List<String> roles;
    UserProfileResponse profile;
    UserSecurityResponse security;
    UserEducationResponse education;
}
