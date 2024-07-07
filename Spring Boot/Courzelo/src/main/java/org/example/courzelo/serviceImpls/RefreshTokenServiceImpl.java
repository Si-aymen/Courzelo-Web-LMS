package org.example.courzelo.serviceImpls;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.RefreshToken;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.RefreshTokenRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.IRefreshTokenService;
import org.example.courzelo.services.IUserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements IRefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final IUserService userService;
    private final UserRepository userRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, IUserService userService, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean ValidToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken != null && refreshToken.getExpiryDate().isAfter(Instant.now())&&userService.ValidUser(refreshToken.getUser().getEmail());
    }

    @Override
    public User getUserFromToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken.getUser();
    }

    @Override
    public RefreshToken createRefreshToken(String email, long expiration) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findUserByEmail(email))
                .token(java.util.UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(expiration))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void deleteUserTokens(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }

    @Override
    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void deleteExpiredTokens() {
        log.info("Deleting expired tokens");
        refreshTokenRepository.deleteAllByExpiryDateBefore(Instant.now());
    }
}
