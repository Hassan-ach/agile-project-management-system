package com.ensa.agile.application.dto.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.user.request.UpdateProfileRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;

public class UpdateProfileRequestTest {

    @Test
    void shouldCreateUpdateProfileRequestSuccessfully_whenValidInput() {
        UpdateProfileRequest req = UpdateProfileRequest.builder()
                                       .id("user-123")
                                       .firstName("Jane")
                                       .lastName("Smith")
                                       .password("newSecurePassword")
                                       .build();

        assertDoesNotThrow(() -> new UpdateProfileRequest(req));
    }

    @Test
    void
    shouldCreateUpdateProfileRequestSuccessfully_whenJustFirstNamePresent() {
        UpdateProfileRequest req = UpdateProfileRequest.builder()
                                       .id("user-123")
                                       .firstName("Jane")
                                       .build();

        assertDoesNotThrow(() -> new UpdateProfileRequest(req));
    }

    @Test
    void
    shouldCreateUpdateProfileRequestSuccessfully_whenJustLastNamePresent() {
        UpdateProfileRequest req = UpdateProfileRequest.builder()
                                       .id("user-123")
                                       .lastName("Smith")
                                       .build();

        assertDoesNotThrow(() -> new UpdateProfileRequest(req));
    }

    @Test
    void
    shouldCreateUpdateProfileRequestSuccessfully_whenJustPasswordPresent() {
        UpdateProfileRequest req = UpdateProfileRequest.builder()
                                       .id("user-123")
                                       .password("newSecurePassword")
                                       .build();

        assertDoesNotThrow(() -> new UpdateProfileRequest(req));
    }
    @Test
    void shouldThrowValidationException_whenAllFieldsAreNull() {
        UpdateProfileRequest req =
            UpdateProfileRequest.builder().id("123").build();

        assertThrows(ValidationException.class,
                     () -> new UpdateProfileRequest(req));
    }

    @Test
    void shouldThrowValidationException_whenIdIsNull() {
        UpdateProfileRequest req =
            UpdateProfileRequest.builder().id(null).firstName("Jane").build();

        assertThrows(ValidationException.class,
                     () -> new UpdateProfileRequest(req));
    }

    @Test
    void shouldThrowValidationException_whenFirstNameIsTooShort() {
        UpdateProfileRequest req = UpdateProfileRequest.builder()
                                       .id("user-123")
                                       .firstName("Ja")
                                       .build();

        assertThrows(ValidationException.class,
                     () -> new UpdateProfileRequest(req));
    }

    @Test
    void shouldThrowValidationException_whenPasswordIsTooShort() {
        UpdateProfileRequest req = UpdateProfileRequest.builder()
                                       .id("user-123")
                                       .firstName("Jane")
                                       .password("123")
                                       .build();

        assertThrows(ValidationException.class,
                     () -> new UpdateProfileRequest(req));
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class,
                     () -> new UpdateProfileRequest(null));
    }
}
