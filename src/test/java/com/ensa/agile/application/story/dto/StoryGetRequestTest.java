package com.ensa.agile.application.story.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.application.story.request.UserStoryGetRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class StoryGetRequestTest {

    @Test
    void shouldCreateGetRequestSuccessfully() {
        assertDoesNotThrow(
            () -> { new UserStoryGetRequest(UUID.randomUUID(), null); });
    }
}
