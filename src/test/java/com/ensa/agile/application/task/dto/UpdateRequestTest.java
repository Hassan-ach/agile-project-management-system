package com.ensa.agile.application.task.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.task.request.TaskUpdateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;

public class UpdateRequestTest {

    @Test
    void shouldCreateTaskUpdateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new TaskUpdateRequest(
                "sprint-123", "us-456", "task-789",
                TaskUpdateRequest.builder().title("Updated Title").build());
        });
    }

    @Test
    void shouldThrowValidationException_whenNoFieldsProvidedForUpdate() {
        assertThrows(ValidationException.class, () -> {
            new TaskUpdateRequest("sprint-123", "us-456", "task-789",
                                  TaskUpdateRequest.builder().build());
        });
    }

    @Test
    void shouldThrowValidationException_whenIdIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new TaskUpdateRequest(
                "sprint-123", "us-456", "",
                TaskUpdateRequest.builder().title("Title").build());
        });
    }

    @Test
    void shouldUpdateActualHoursSuccessfully_whenPositive() {
        assertDoesNotThrow(() -> {
            new TaskUpdateRequest(
                "sprint-123", "us-456", "task-789",
                TaskUpdateRequest.builder().actualHours(5.5).build());
        });
    }

    @Test
    void shouldThrowValidationException_whenTitleIsProvidedAsBlank() {
        assertThrows(ValidationException.class, () -> {
            new TaskUpdateRequest(
                "sprint-123", "us-456", "task-789",
                TaskUpdateRequest.builder().title(" ").build());
        });
    }

    @Test
    void shouldThrowValidationException_whenAssigneeEmailIsInvalid() {
        assertThrows(ValidationException.class, () -> {
            new TaskUpdateRequest("sprint-123", "us-456", "task-789",
                                  TaskUpdateRequest.builder()
                                      .assigneeEmail("invalid-email")
                                      .build());
        });
    }
}
