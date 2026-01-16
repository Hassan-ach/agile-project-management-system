package com.ensa.agile.application.sprint.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.sprint.request.SprintBackLogCreateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class CreateRequestTest {

    @Test
    void shouldCreateSprintBackLogCreateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new SprintBackLogCreateRequest(
                UUID.randomUUID(),
                SprintBackLogCreateRequest.builder()
                    .name("Sprint 1")
                    .scrumMasterEmail("master@ensa.ma")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(14))
                    .goal("Finish MVP")
                    .userStoriesIds(List.of(UUID.randomUUID()))
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenNameIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogCreateRequest(
                UUID.randomUUID(), SprintBackLogCreateRequest.builder()
                                       .name(" ")
                                       .scrumMasterEmail("master@ensa.ma")
                                       .startDate(LocalDate.now())
                                       .endDate(LocalDate.now().plusDays(14))
                                       .goal("Goal")
                                       .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenEmailIsInvalid() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogCreateRequest(
                UUID.randomUUID(), SprintBackLogCreateRequest.builder()
                                       .name("Sprint 1")
                                       .scrumMasterEmail("invalid-email")
                                       .startDate(LocalDate.now())
                                       .endDate(LocalDate.now().plusDays(14))
                                       .goal("Goal")
                                       .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenEndDateIsBeforeStartDate() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogCreateRequest(
                UUID.randomUUID(), SprintBackLogCreateRequest.builder()
                                       .name("Sprint 1")
                                       .scrumMasterEmail("master@ensa.ma")
                                       .startDate(LocalDate.now())
                                       .endDate(LocalDate.now().minusDays(1))
                                       .goal("Goal")
                                       .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class, () -> {
            new SprintBackLogCreateRequest(UUID.randomUUID(), null);
        });
    }
}
