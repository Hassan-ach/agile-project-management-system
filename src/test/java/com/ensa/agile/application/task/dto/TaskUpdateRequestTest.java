package com.ensa.agile.application.task.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.task.request.TaskUpdateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class TaskUpdateRequestTest {

    @Test
    void shouldCreateTaskUpdateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new TaskUpdateRequest( UUID.randomUUID(),
                TaskUpdateRequest.builder().title("Updated Title").build());
        });
    }

    @Test
    void shouldThrowValidationException_whenNoFieldsProvidedForUpdate() {
        assertThrows(ValidationException.class, () -> {
            new TaskUpdateRequest(UUID.randomUUID(),
                                  TaskUpdateRequest.builder().build());
        });
    }

    @Test
    void shouldUpdateActualHoursSuccessfully_whenPositive() {
        assertDoesNotThrow(() -> {
            new TaskUpdateRequest( UUID.randomUUID(),
                TaskUpdateRequest.builder().actualHours(5.5).build());
        });
    }

    @Test
    void shouldThrowValidationException_whenTitleIsProvidedAsBlank() {
        assertThrows(ValidationException.class, () -> {
            new TaskUpdateRequest( UUID.randomUUID(),
                TaskUpdateRequest.builder().title(" ").build());
        });
    }

}
