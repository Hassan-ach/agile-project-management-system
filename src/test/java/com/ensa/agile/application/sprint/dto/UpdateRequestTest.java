package com.ensa.agile.application.dto.sprint;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.sprint.request.SprintBackLogUpdateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class UpdateRequestTest {

    @Test
    void shouldCreateSprintBackLogUpdateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new SprintBackLogUpdateRequest(
                "sprint-123", SprintBackLogUpdateRequest.builder()
                    .name("Updated Sprint Name")
                    .goal("Updated Goal")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenAllFieldsAreNull() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogUpdateRequest(
                "sprint-123", SprintBackLogUpdateRequest.builder().build());
        });
    }

    @Test
    void shouldThrowValidationException_whenNameIsProvidedAsBlank() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogUpdateRequest(
                "sprint-123",
                SprintBackLogUpdateRequest.builder().name(" ").build());
        });
    }

    @Test
    void shouldUpdateDateSuccessfully_whenDatesAreProvided() {
        assertDoesNotThrow(() -> {
            new SprintBackLogUpdateRequest(
                "sprint-123", SprintBackLogUpdateRequest.builder()
                                  .startDate(LocalDate.now())
                                  .endDate(LocalDate.now().plusDays(7))
                                  .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogUpdateRequest("sprint-123", null);
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
