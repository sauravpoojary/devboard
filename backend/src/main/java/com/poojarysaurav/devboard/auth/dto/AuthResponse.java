package com.poojarysaurav.devboard.auth.dto;

public record AuthResponse(
        String token,
        String email,
        String name
) {}
