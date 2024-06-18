package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.User;

@Data
public class UserResponse {
    String email;

    public UserResponse(String email) {
        this.email = email;
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getEmail());
    }
}
