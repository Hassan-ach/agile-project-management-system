package com.ensa.agile.application.task.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.task.request.TaskCreateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class CreateRequestTest {

    @Test
    void shouldCreateTaskCreateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new TaskCreateRequest(UUID.randomUUID(),
                TaskCreateRequest.builder()
                    .title("Fix Bug")
                    .description("Fix the login timeout issue")
                    .estimatedHours(4.0)
                    .assigneeEmail("dev@ensa.ma")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenEstimatedHoursIsZeroOrNegative() {
        assertThrows(ValidationException.class, () -> {
            new TaskCreateRequest(UUID.randomUUID(),
                                  TaskCreateRequest.builder()
                                      .title("Task")
                                      .description("Desc")
                                      .estimatedHours(0.0)
                                      .assigneeEmail("dev@ensa.ma")
                                      .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenEmailFormatIsInvalid() {
        assertThrows(ValidationException.class, () -> {
            new TaskCreateRequest(UUID.randomUUID(),
                                  TaskCreateRequest.builder()
                                      .title("Task")
                                      .description("Desc")
                                      .estimatedHours(2.0)
                                      .assigneeEmail("not-an-email")
                                      .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class, () -> {
            new TaskCreateRequest(UUID.randomUUID(), null);
        });
    }
}
