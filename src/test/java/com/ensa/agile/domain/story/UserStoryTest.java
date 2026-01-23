package com.ensa.agile.domain.story;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.enums.MoscowType;
import com.ensa.agile.testfactory.TestProductBackLogFactory;

public class UserStoryTest {
    @ParameterizedTest
    @CsvSource(
        {"User Story Title, User Story Description, MUST_HAVE, 54, User "
             + "Story Acceptance Criteria, true",
         "'', User Story Description, MUST_HAVE, 54, User Story Acceptance "
             + "Criteria, false",
         "User Story Title, '', MUST_HAVE, 54, User Story Acceptance "
             + "Criteria, false",
         "User Story Title, User Story Description, MUST_HAVE, 54, '', false",
         "User Story Title, User Story Description, MUST_HAVE, 54, , false"

         ,
         "User Story Title, User Story Description, MUST_HAVE, , User Story "
             + "Acceptance Criteria, false"

         ,
         "User Story Title, User Story Description, MUST_HAVE, -10, User "
             + "Story Acceptance Criteria, false",
         "User Story Title, User Story Description, MUST_HAVE, 110, User "
             + "Story Acceptance Criteria, false"})
    void shouldValidateUserStoryCreation(String title, String description,
                                         String moscowType, Integer storyPoints,
                                         String acceptanceCriteria,
                                         boolean expectedValidity) {
        if (expectedValidity) {
            assertDoesNotThrow(() -> {
                UserStory.builder()
                    .title(title)
                    .description(description)
                    .priority(MoscowType.valueOf(moscowType))
                    .storyPoints(storyPoints)
                    .acceptanceCriteria(acceptanceCriteria)
                    .productBackLog(TestProductBackLogFactory.validProduct())
                    .build();
            });
        } else {
            assertThrows(ValidationException.class, () -> {
                UserStory.builder()
                    .title(title)
                    .description(description)
                    .priority(MoscowType.valueOf(moscowType))
                    .storyPoints(storyPoints)
                    .acceptanceCriteria(acceptanceCriteria)
                    .productBackLog(TestProductBackLogFactory.validProduct())
                    .build();
            });
        }
    }

    @ParameterizedTest
    @CsvSource({"Updated Title, Updated Description, MUST_HAVE, 34, Updated "
                    + "Acceptance Criteria, true",
                "'', Updated Description, MUST_HAVE, 34, Updated Acceptance "
                    + "Criteria, false",
                "Updated Title, '', MUST_HAVE, 34, Updated Acceptance "
                    + "Criteria, false",
                "Updated Title, Updated Description, MUST_HAVE, 34, '', false",
                "Updated Title, Updated Description, MUST_HAVE, 34, , true",
                "Updated Title, Updated Description, MUST_HAVE, -5, Updated "
                    + "Acceptance "
                    + "Criteria, false",
                "Updated Title, Updated Description, MUST_HAVE, 150, Updated "
                    + "Acceptance "
                    + "Criteria, false",
                "Updated Title, Updated Description, , 34, Updated "
                    + "Acceptance Criteria, true"})
    void shouldValidateUserStoryUpdate(String title, String description,
                                       String moscowType, Integer storyPoints,
                                       String acceptanceCriteria,
                                       boolean expectedValidity) {
        UserStory userStory =
            UserStory.builder()
                .title("Initial Title")
                .description("Initial Description")
                .priority(MoscowType.MUST_HAVE)
                .storyPoints(20)
                .acceptanceCriteria("Initial Acceptance Criteria")
                .productBackLog(TestProductBackLogFactory.validProduct())
                .build();

        if (expectedValidity) {
            assertDoesNotThrow(() -> {
                userStory.updateMetaData(
                    title, description,
                    moscowType != null ? MoscowType.valueOf(moscowType) : null,
                    storyPoints, acceptanceCriteria);
            });

            if (title != null && !title.isEmpty())
                assert (userStory.getTitle().equals(title));
            if (description != null && !description.isEmpty())
                assert (userStory.getDescription().equals(description));
            if (moscowType != null)
                assert (userStory.getPriority().equals(
                    MoscowType.valueOf(moscowType)));
            if (storyPoints != null)
                assert (userStory.getStoryPoints().equals(storyPoints));
            if (acceptanceCriteria != null && !acceptanceCriteria.isEmpty())
                assert (userStory.getAcceptanceCriteria().equals(
                    acceptanceCriteria));

        } else {
            assertThrows(ValidationException.class, () -> {
                userStory.updateMetaData(
                    title, description,
                    moscowType != null ? MoscowType.valueOf(moscowType) : null,
                    storyPoints, acceptanceCriteria);
            });
        }
    }
}
