package org.example.courzelo.services;

import org.example.courzelo.models.RefreshToken;


public interface IRefreshTokenService {
    boolean ValidToken(String token);
    RefreshToken createRefreshToken(String email, long expiration);
}
