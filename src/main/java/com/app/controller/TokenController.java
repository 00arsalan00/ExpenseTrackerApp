package com.app.controller;

import org.springframework.web.bind.annotation.*;

import com.app.entity.RefreshToken;
import com.app.request.RefreshTokenRequestDto;
import com.app.response.JwtResponseDto;
import com.app.service.JwtService;
import com.app.service.RefreshTokenService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/auth/v1")
public class TokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/refreshToken")
    public JwtResponseDto refreshToken(
            @RequestBody RefreshTokenRequestDto requestDto) {

        return refreshTokenService.findByToken(requestDto.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserDetails)
                .map(user -> {

                    String newAccessToken =
                            jwtService.generateToken(user.getUsername());

                    return JwtResponseDto.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(requestDto.getRefreshToken())
                            .build();
                })
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found"));
    }
}
