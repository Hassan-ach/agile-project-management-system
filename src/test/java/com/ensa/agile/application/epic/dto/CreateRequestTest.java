package com.ensa.agile.application.dtos.epic.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.epic.request.EpicCreateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;

public class CreateRequestTest {

    @Test
    void shouldCreateEpicCreateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new EpicCreateRequest(
                "prod-123",
                EpicCreateRequest.builder()
                    .title("Core Features")
                    .description("Initial epic for core functionality")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenTitleIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new EpicCreateRequest("prod-123", EpicCreateRequest.builder()
                                                  .title(" ")
                                                  .description("Description")
                                                  .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenDescriptionIsNull() {
        assertThrows(ValidationException.class, () -> {
            new EpicCreateRequest("prod-123", EpicCreateRequest.builder()
                                                  .title("Title")
                                                  .description(null)
                                                  .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class,
                     () -> { new EpicCreateRequest("prod-123", null); });
    }
}
