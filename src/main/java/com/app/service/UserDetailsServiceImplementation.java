package com.app.service;

import java.util.HashSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.model.UserDetailsDto;
import com.app.repository.UserRepository;

@Component
public class UserDetailsServiceImplementation implements UserDetailsService {

    private static final Logger log =
            LoggerFactory.getLogger(UserDetailsServiceImplementation.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImplementation(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= AUTHENTICATION =================

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        log.debug("Loading user by username: {}", username);

        com.app.entity.UserDetails user =
                userRepository.findByUsername(username);

        if (user == null) {
            log.error("User not found: {}", username);
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user);
    }

    // ================= SIGNUP =================

    public boolean signupUser(UserDetailsDto dto) {

        if (!isValidEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!isValidPassword(dto.getPassword())) {
            throw new IllegalArgumentException("Weak password");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalStateException("Username already taken");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        com.app.entity.UserDetails user =
                new com.app.entity.UserDetails(
                        UUID.randomUUID().toString(),
                        dto.getUsername(),
                        passwordEncoder.encode(dto.getPassword()),
                        dto.getEmail(),
                        new HashSet<>()
                );

        userRepository.save(user);
        return true;
    }

    // ================= VALIDATIONS =================

    private boolean isValidEmail(String email) {
        if (email == null) return false;

        String emailRegex =
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;

        String passwordRegex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";

        return password.matches(passwordRegex);
    }
}
