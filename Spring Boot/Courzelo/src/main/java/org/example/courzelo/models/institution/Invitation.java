package org.example.courzelo.models.institution;

import lombok.Data;
import org.example.courzelo.models.Role;

import java.time.Instant;
@Data
public class Invitation {
    private String code;
    private String email;
    private Role role;
    private Instant expiryDate;
}
