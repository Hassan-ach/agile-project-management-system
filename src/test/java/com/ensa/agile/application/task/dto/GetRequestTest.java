package com.ensa.agile.application.dtos.task.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.application.task.request.TaskGetRequest;
import org.junit.jupiter.api.Test;

public class GetRequestTest {

    @Test
    void shouldCreateTaskGetRequestSuccessfully() {
        assertDoesNotThrow(() -> {
            new TaskGetRequest("task-123", null);
        });
    }
}
