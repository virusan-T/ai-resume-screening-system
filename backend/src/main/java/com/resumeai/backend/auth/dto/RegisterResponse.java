package com.resumeai.backend.auth.dto;

public record RegisterResponse(
        Long userId,
        String fullName,
        String email,
        String role,
        String message
) {
}