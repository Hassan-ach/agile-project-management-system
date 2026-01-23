package com.ensa.agile.application.epic.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.epic.request.EpicUpdateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class EpicUpdateRequestTest {

    @Test
    void shouldCreateEpicUpdateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new EpicUpdateRequest(UUID.randomUUID(),
                                  EpicUpdateRequest.builder()
                                      .title("New Title")
                                      .description("New Description")
                                      .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenBothTitleAndDescriptionAreNull() {
        assertThrows(ValidationException.class, () -> {
            new EpicUpdateRequest(UUID.randomUUID(),
                                  EpicUpdateRequest.builder()
                                      .title(null)
                                      .description(null)
                                      .build());
        });
    }

    @Test
    void shouldUpdateOnlyTitleSuccessfully_whenDescriptionIsNull() {
        assertDoesNotThrow(() -> {
            new EpicUpdateRequest(
              UUID.randomUUID(),
                EpicUpdateRequest.builder().title("Updated Title").build());
        });
    }

    @Test
    void shouldThrowValidationException_whenTitleIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new EpicUpdateRequest(
               UUID.randomUUID(),
                EpicUpdateRequest.builder().title("").build());
        });
    }
}
