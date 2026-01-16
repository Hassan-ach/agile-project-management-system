package com.ensa.agile.application.story.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.application.story.request.UserStoryGetRequest;
import org.junit.jupiter.api.Test;

public class GetRequestTest {

    @Test
    void shouldCreateGetRequestSuccessfully() {
        assertDoesNotThrow(() -> {
            new UserStoryGetRequest("us-123", null);
        });
    }
}
