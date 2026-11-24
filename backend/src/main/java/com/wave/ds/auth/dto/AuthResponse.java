package com.wave.ds.auth.dto;

public record AuthResponse(String token, String tokenType, long expiresInSeconds, UserResponse user) {
}
