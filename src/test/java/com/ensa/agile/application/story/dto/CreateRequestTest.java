package com.ensa.agile.application.dtos.story.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.story.request.UserStoryCreateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.story.enums.MoscowType;
import org.junit.jupiter.api.Test;

public class CreateRequestTest {

    @Test
    void shouldCreateUserStoryCreateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new UserStoryCreateRequest(
                "prod-123",
                UserStoryCreateRequest.builder()
                    .title("User Auth")
                    .description("Enable login via OAuth")
                    .priority(MoscowType.MUST_HAVE)
                    .storyPoints(5)
                    .acceptanceCriteria("User can login with Google")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenProductIdIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new UserStoryCreateRequest(
                " ",
                UserStoryCreateRequest.builder()
                    .title("Title")
                    .description("Desc")
                    .priority(MoscowType.COULD_HAVE)
                    .storyPoints(1)
                    .acceptanceCriteria("AC")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenStoryPointsIsLessThanOne() {
        assertThrows(ValidationException.class, () -> {
            new UserStoryCreateRequest(
                "prod-123",
                UserStoryCreateRequest.builder()
                    .title("Title")
                    .description("Desc")
                    .priority(MoscowType.MUST_HAVE)
                    .storyPoints(0)
                    .acceptanceCriteria("AC")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenPriorityIsNull() {
        assertThrows(ValidationException.class, () -> {
            new UserStoryCreateRequest(
                "prod-123",
                UserStoryCreateRequest.builder()
                    .title("Title")
                    .description("Desc")
                    .priority(null)
                    .storyPoints(3)
                    .acceptanceCriteria("AC")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class, () -> {
            new UserStoryCreateRequest("prod-123", null);
        });
    }
}
