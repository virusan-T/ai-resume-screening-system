package com.resumeai.backend.auth.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTests {
    private static final String SECRET = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    @Test
    void generatesAndValidatesTokenWithExpectedClaims() {
        JwtService service = jwtService(Duration.ofMinutes(5));

        String token = service.generateToken(7L, "candidate@example.com", "CANDIDATE");

        assertEquals("candidate@example.com", service.extractEmail(token));
        assertEquals(7L, service.extractUserId(token));
        assertEquals("CANDIDATE", service.extractRole(token));
        assertTrue(service.isTokenValid(token, "candidate@example.com"));
    }

    @Test
    void rejectsExpiredToken() {
        JwtService service = jwtService(Duration.ofSeconds(-1));
        String token = service.generateToken(7L, "candidate@example.com", "CANDIDATE");

        assertThrows(ExpiredJwtException.class, () -> service.extractEmail(token));
    }

    private JwtService jwtService(Duration expiration) {
        JwtService service = new JwtService(SECRET, expiration);
        service.initializeSigningKey();
        return service;
    }
}
