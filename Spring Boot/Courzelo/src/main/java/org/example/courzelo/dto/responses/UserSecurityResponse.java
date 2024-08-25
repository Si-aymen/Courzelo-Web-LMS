package org.example.courzelo.dto.responses;

import lombok.Builder;
import lombok.Data;
import org.example.courzelo.models.UserSecurity;

@Data
public class UserSecurityResponse {
    private boolean twoFactorAuthEnabled;
    private boolean enabled;
    private Boolean ban;

    public UserSecurityResponse(UserSecurity userSecurity) {
        this.twoFactorAuthEnabled = userSecurity.isTwoFactorAuthEnabled();
        this.enabled = userSecurity.isEnabled();
        this.ban = userSecurity.getBan();
    }
}
