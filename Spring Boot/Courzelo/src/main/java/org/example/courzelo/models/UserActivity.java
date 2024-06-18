package org.example.courzelo.models;

import lombok.Data;

import java.time.Instant;

@Data
public class UserActivity {
    private Instant CreatedAt = Instant.now();
    private Instant lastActivity;
    private Instant lastLogin;
    private Instant lastLogout;
}
