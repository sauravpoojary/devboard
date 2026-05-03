package com.poojarysaurav.devboard.auth;

import com.poojarysaurav.devboard.auth.dto.AuthResponse;
import com.poojarysaurav.devboard.auth.dto.LoginRequest;
import com.poojarysaurav.devboard.auth.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public AuthResponse register(RegisterRequest request) {
        // TODO: implement registration logic
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public AuthResponse login(LoginRequest request) {
        // TODO: implement login logic
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
