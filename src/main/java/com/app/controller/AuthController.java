package com.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.entity.RefreshToken;
import com.app.model.UserDetailsDto;
import com.app.request.AuthRequestDto;
import com.app.response.JwtResponseDto;
import com.app.service.JwtService;
import com.app.service.RefreshTokenService;
import com.app.service.UserDetailsServiceImplementation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Authentication APIs", description = "User Authentication and token management")
@RestController
@AllArgsConstructor
@RequestMapping("/auth/v1")
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImplementation userDetailsService;
    private final AuthenticationManager authenticationManager;

    // ---------------- SIGNUP ----------------

    @PostMapping("/signup")
    public ResponseEntity<JwtResponseDto> signup(
            @RequestBody UserDetailsDto userDetailsDto) {

        Boolean isSignedUp = userDetailsService.signupUser(userDetailsDto);

        if (!isSignedUp) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        String accessToken =
                jwtService.generateToken(userDetailsDto.getUsername());

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(
                        userDetailsDto.getUsername());

        JwtResponseDto response = JwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();

        return ResponseEntity.ok(response);
    }

    // ---------------- LOGIN ----------------

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> authenticateAndGetToken(
            @RequestBody AuthRequestDto authRequestDto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequestDto.getUsername(),
                                authRequestDto.getPassword()
                        )
                );

        String accessToken =
                jwtService.generateToken(authRequestDto.getUsername());

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(
                        authRequestDto.getUsername());

        JwtResponseDto response = JwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();

        return ResponseEntity.ok(response);
    }
}
