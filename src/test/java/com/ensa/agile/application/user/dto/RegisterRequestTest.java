package com.ensa.agile.application.dto.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.user.request.RegisterRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;

public class RegisterRequestTest {

    @Test
    void shouldCreateRegisterRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new RegisterRequest(
                RegisterRequest.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@ensa.ma")
                    .password("securePassword123")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenFirstNameIsTooShort() {
        assertThrows(ValidationException.class, () -> {
            new RegisterRequest(
                RegisterRequest.builder()
                    .firstName("Jo")
                    .lastName("Doe")
                    .email("john@ensa.ma")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenPasswordIsTooShort() {
        assertThrows(ValidationException.class, () -> {
            new RegisterRequest(
                RegisterRequest.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("john@ensa.ma")
                    .password("short")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenEmailIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new RegisterRequest(
                RegisterRequest.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email(" ")
                    .build());
        });
    }
}
