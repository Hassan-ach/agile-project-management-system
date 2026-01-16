package com.ensa.agile.application.dto.sprint;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.application.sprint.request.SprintBackLogGetRequest;
import org.junit.jupiter.api.Test;

public class GetRequestTest {

    @Test
    void shouldCreateGetRequestSuccessfully_whenWithIsNull() {
        assertDoesNotThrow(() -> {
            new SprintBackLogGetRequest("sprint-123", null);
        });
    }
}
