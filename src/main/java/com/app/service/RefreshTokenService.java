package com.app.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.RefreshToken;
import com.app.entity.UserDetails;
import com.app.repository.RefreshTokenRepository;
import com.app.repository.UserRepository;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public RefreshToken createRefreshToken(String username) {

        UserDetails user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();

        RefreshToken refreshToken = RefreshToken.builder()
                .userDetails(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(60 * 60 * 24))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(
                    "Refresh token expired. Please login again."
            );
        }

        return token;
    }

    @Transactional
    public void deleteByUser(UserDetails user) {
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();
    }
}
