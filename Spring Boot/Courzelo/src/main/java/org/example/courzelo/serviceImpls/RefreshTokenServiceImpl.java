package org.example.courzelo.serviceImpls;

import org.example.courzelo.models.RefreshToken;
import org.example.courzelo.repositories.RefreshTokenRepository;
import org.example.courzelo.services.IRefreshTokenService;
import org.example.courzelo.services.IUserService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final IUserService userService;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, IUserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Override
    public boolean ValidToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken != null && refreshToken.getExpiryDate().isAfter(Instant.now())&&userService.ValidUser(refreshToken.getUser().getEmail());
    }
}
