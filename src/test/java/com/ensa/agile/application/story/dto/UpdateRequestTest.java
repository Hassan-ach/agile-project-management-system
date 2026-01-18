package com.ensa.agile.application.story.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.story.request.UserStoryUpdateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class UpdateRequestTest {

    @Test
    void shouldCreateUserStoryUpdateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new UserStoryUpdateRequest(UUID.randomUUID(),
                                       UserStoryUpdateRequest.builder()
                                           .title("Updated Title")
                                           .storyPoints(8)
                                           .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenIdIsNull() {
        assertThrows(ValidationException.class, () -> {
            new UserStoryUpdateRequest( null,
                UserStoryUpdateRequest.builder().title("Title").build());
        });
    }

    @Test
    void shouldThrowValidationException_whenNoFieldsProvidedForUpdate() {
        assertThrows(ValidationException.class, () -> {
            new UserStoryUpdateRequest(UUID.randomUUID(),
                UserStoryUpdateRequest.builder().build());
        });
    }

    @Test
    void shouldThrowValidationException_whenStoryPointsIsInvalidInUpdate() {
        assertThrows(ValidationException.class, () -> {
            new UserStoryUpdateRequest(UUID.randomUUID(),
                UserStoryUpdateRequest.builder().storyPoints(0).build());
        });
    }

    @Test
    void shouldUpdateOnlyAcceptanceCriteriaSuccessfully() {
        assertDoesNotThrow(() -> {
            new UserStoryUpdateRequest(UUID.randomUUID(),
                                       UserStoryUpdateRequest.builder()
                                           .acceptanceCriteria("New criteria")
                                           .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenTitleIsProvidedAsBlank() {
        assertThrows(ValidationException.class, () -> {
            new UserStoryUpdateRequest(
            UUID.randomUUID(),
                UserStoryUpdateRequest.builder().title(" ").build());
        });
    }
}
