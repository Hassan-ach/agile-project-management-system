package com.ensa.agile.application.dtos.epic.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.epic.request.EpicUpdateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;

public class UpdateRequestTest {

    @Test
    void shouldCreateEpicUpdateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new EpicUpdateRequest("prod-123", "epic-456",
                                  EpicUpdateRequest.builder()
                                      .title("New Title")
                                      .description("New Description")
                                      .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenBothTitleAndDescriptionAreNull() {
        assertThrows(ValidationException.class, () -> {
            new EpicUpdateRequest("prod-123", "epic-456",
                                  EpicUpdateRequest.builder()
                                      .title(null)
                                      .description(null)
                                      .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenIdIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new EpicUpdateRequest(
                "prod-123", " ",
                EpicUpdateRequest.builder().title("Title").build());
        });
    }

    @Test
    void shouldUpdateOnlyTitleSuccessfully_whenDescriptionIsNull() {
        assertDoesNotThrow(() -> {
            new EpicUpdateRequest(
                "prod-123", "epic-456",
                EpicUpdateRequest.builder().title("Updated Title").build());
        });
    }

    @Test
    void shouldThrowValidationException_whenTitleIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new EpicUpdateRequest(
                "prod-123", "epic-456",
                EpicUpdateRequest.builder().title("").build());
        });
    }
}
