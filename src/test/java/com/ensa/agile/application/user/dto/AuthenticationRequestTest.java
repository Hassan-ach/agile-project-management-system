package com.ensa.agile.application.dto.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.user.request.AuthenticationRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;

public class AuthenticationRequestTest {

    @Test
    void shouldCreateAuthenticationRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new AuthenticationRequest(
                AuthenticationRequest.builder()
                    .email("user@ensa.ma")
                    .password("password123")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenEmailIsInvalid() {
        assertThrows(ValidationException.class, () -> {
            new AuthenticationRequest(
                AuthenticationRequest.builder()
                    .email("invalid-email")
                    .password("password123")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenPasswordIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new AuthenticationRequest(
                AuthenticationRequest.builder()
                    .email("user@ensa.ma")
                    .password("   ")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class, () -> {
            new AuthenticationRequest(null);
        });
    }
}
