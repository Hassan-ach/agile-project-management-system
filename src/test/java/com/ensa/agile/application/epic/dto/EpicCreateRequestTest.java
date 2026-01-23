package com.ensa.agile.application.epic.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.epic.request.EpicCreateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class EpicCreateRequestTest {

    @Test
    void shouldCreateEpicCreateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new EpicCreateRequest(
                UUID.randomUUID(),
                EpicCreateRequest.builder()
                    .title("Core Features")
                    .description("Initial epic for core functionality")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenTitleIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new EpicCreateRequest(UUID.randomUUID(),
                                  EpicCreateRequest.builder()
                                      .title(" ")
                                      .description("Description")
                                      .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenDescriptionIsNull() {
        assertThrows(ValidationException.class, () -> {
            new EpicCreateRequest(UUID.randomUUID(), EpicCreateRequest.builder()
                                                         .title("Title")
                                                         .description(null)
                                                         .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class,
                     () -> { new EpicCreateRequest(UUID.randomUUID(), null); });
    }
}
