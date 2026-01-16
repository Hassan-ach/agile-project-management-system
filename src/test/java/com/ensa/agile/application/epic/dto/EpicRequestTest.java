package com.ensa.agile.application.epic.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.application.epic.request.EpicRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class EpicRequestTest {

    @Test
    void shouldCreateEpicRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new EpicRequest(
                UUID.randomUUID(),
                EpicRequest.builder().epicId(UUID.randomUUID()).build());
        });
    }
}
