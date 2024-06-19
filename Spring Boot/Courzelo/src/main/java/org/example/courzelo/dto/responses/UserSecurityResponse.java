package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.UserSecurity;

@Data
public class UserSecurityResponse {
    private boolean twoFactorAuthEnabled;

    public UserSecurityResponse(UserSecurity userSecurity) {
        this.twoFactorAuthEnabled = userSecurity.isTwoFactorAuthEnabled();
    }
}
