package org.example.courzelo.services;

import org.example.courzelo.models.RefreshToken;
import org.example.courzelo.models.User;


public interface IRefreshTokenService {
    boolean ValidToken(String token);
    User getUserFromToken(String token);
    RefreshToken createRefreshToken(String email, long expiration);
}
