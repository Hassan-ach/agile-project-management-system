package com.ensa.agile.application.sprint.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.application.sprint.request.SprintBackLogGetRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class SprintGetRequestTest {

    @Test
    void shouldCreateGetRequestSuccessfully_whenWithIsNull() {
        assertDoesNotThrow(
            () -> { new SprintBackLogGetRequest(UUID.randomUUID(), null); });
    }
}
