package com.ensa.agile.application.epic.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.application.epic.request.EpicGetRequest;
import org.junit.jupiter.api.Test;

public class GetRequestTest {

    @Test
    void shouldCreateEpicGetRequestSuccessfully() {
        assertDoesNotThrow(
            () -> { new EpicGetRequest("prod-123", "epic-456", null); });
    }
}
