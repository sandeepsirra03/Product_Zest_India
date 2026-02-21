package com.zest.productapi.service;

import com.zest.productapi.dto.AuthenticationRequest;
import com.zest.productapi.dto.AuthenticationResponse;
import com.zest.productapi.dto.RegisterRequest;
import com.zest.productapi.entity.Role;
import com.zest.productapi.entity.User;
import com.zest.productapi.repository.UserRepository;
import com.zest.productapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var user = User.builder()
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(request.getRole() != null ? request.getRole() : Role.USER)
                                .build();
                repository.save(user);

                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);

                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .refreshToken(refreshToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow();

                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);

                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .refreshToken(refreshToken)
                                .build();
        }

        public AuthenticationResponse refreshToken(jakarta.servlet.http.HttpServletRequest request) {
                final String authHeader = request.getHeader("Authorization");
                final String refreshToken;
                final String userEmail;
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        throw new RuntimeException("Refresh token is missing");
                }
                refreshToken = authHeader.substring(7);
                userEmail = jwtService.extractUsername(refreshToken);
                if (userEmail != null) {
                        var user = repository.findByEmail(userEmail)
                                        .orElseThrow();
                        if (jwtService.isTokenValid(refreshToken, user)) {
                                var accessToken = jwtService.generateToken(user);
                                return AuthenticationResponse.builder()
                                                .accessToken(accessToken)
                                                .refreshToken(refreshToken)
                                                .build();
                        }
                }
                throw new RuntimeException("Invalid refresh token");
        }
}
