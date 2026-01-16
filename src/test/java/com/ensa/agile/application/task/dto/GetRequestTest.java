package com.ensa.agile.application.task.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.ensa.agile.application.task.request.TaskGetRequest;

public class GetRequestTest {

    @Test
    void shouldCreateTaskGetRequestSuccessfully() {
        assertDoesNotThrow(
            () -> { new TaskGetRequest(UUID.randomUUID(), null); });
    }
}
