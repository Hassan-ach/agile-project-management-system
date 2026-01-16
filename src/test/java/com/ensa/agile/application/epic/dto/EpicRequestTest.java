package com.ensa.agile.application.epic.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.epic.request.EpicRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;

public class EpicRequestTest {

    @Test
    void shouldCreateEpicRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new EpicRequest(
                "prod-123",
                EpicRequest.builder().epicId("epic-456").build()
            );
        });
    }

    @Test
    void shouldThrowValidationException_whenEpicIdIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new EpicRequest(
                "prod-123",
                EpicRequest.builder().epicId("").build()
            );
        });
    }

    @Test
    void shouldThrowValidationException_whenProductIdIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new EpicRequest(
                " ",
                EpicRequest.builder().epicId("epic-456").build()
            );
        });
    }
}
