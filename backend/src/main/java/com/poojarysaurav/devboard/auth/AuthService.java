package com.poojarysaurav.devboard.auth;

import com.poojarysaurav.devboard.auth.dto.AuthResponse;
import com.poojarysaurav.devboard.auth.dto.LoginRequest;
import com.poojarysaurav.devboard.auth.dto.RegisterRequest;
import com.poojarysaurav.devboard.common.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use: " + request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(user.getId(), token, user.getEmail(), user.getName());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication"));

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(user.getId(), token, user.getEmail(), user.getName());
    }
}
