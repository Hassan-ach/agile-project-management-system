package com.ensa.agile.application.sprint.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.sprint.request.SprintBackLogUpdateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class SprintUpdateRequestTest {

    @Test
    void shouldCreateSprintBackLogUpdateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new SprintBackLogUpdateRequest(UUID.randomUUID(),
                                           SprintBackLogUpdateRequest.builder()
                                               .name("Updated Sprint Name")
                                               .goal("Updated Goal")
                                               .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenAllFieldsAreNull() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogUpdateRequest(
                UUID.randomUUID(),
                SprintBackLogUpdateRequest.builder().build());
        });
    }

    @Test
    void shouldThrowValidationException_whenNameIsProvidedAsBlank() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogUpdateRequest(
                UUID.randomUUID(),
                SprintBackLogUpdateRequest.builder().name(" ").build());
        });
    }

    @Test
    void shouldUpdateDateSuccessfully_whenDatesAreProvided() {
        assertDoesNotThrow(() -> {
            new SprintBackLogUpdateRequest(
                UUID.randomUUID(), SprintBackLogUpdateRequest.builder()
                                       .startDate(LocalDate.now())
                                       .endDate(LocalDate.now().plusDays(7))
                                       .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogUpdateRequest(UUID.randomUUID(), null);
        });
    }

    @Test
    void shouldThrowValidationException_whenIdIsNull() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogUpdateRequest(null,
                                           SprintBackLogUpdateRequest.builder()
                                               .name("Updated Sprint Name")
                                               .goal("Updated Goal")
                                               .build());
        });
    }
}
