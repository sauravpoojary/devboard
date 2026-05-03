package com.poojarysaurav.devboard.auth.dto;

public record AuthResponse(
        Long id,
        String token,
        String email,
        String name
) {}
