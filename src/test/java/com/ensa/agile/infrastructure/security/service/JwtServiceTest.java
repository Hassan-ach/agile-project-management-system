package com.ensa.agile.infrastructure.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(
            2L, 24L,
            "fc8153c3bfca60f62af67835b8118e9710af4e065cbab2b24f3ffdd3193e1465");
    }

    @Test
    void testTokenLifecycle() {
        String email = "test@gmail.com";

        String token = jwtService.generateToken(email);
        assertNotNull(token);
        assertEquals(false, jwtService.isTokenExpired(token));
        assertEquals(email, jwtService.extractEmail(token));
    }
}
