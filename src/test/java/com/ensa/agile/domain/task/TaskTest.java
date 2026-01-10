package com.ensa.agile.domain.task;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.testfactory.TestSprintBackLogFactory;
import com.ensa.agile.testfactory.TestUserFactory;
import com.ensa.agile.testfactory.TestUserStoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TaskTest {

    @Test
    void souldCreateTaskSuccessfully_whenAllFieldsAreValid() {
        assertDoesNotThrow(
            ()
                -> Task.builder()
                       .title("Implement login feature")
                       .description("Create login functionality using OAuth2")
                       .userStory(TestUserStoryFactory.validUserStory())
                       .sprintBackLog(
                           TestSprintBackLogFactory.validSprintBackLog())
                       .assignee(TestUserFactory.validUser())
                       .estimatedHours(8.00)
                       .build());
    }

    @Test
    void souldFailToCreateTask_whenUserStoryIsNull() {
        assertThrows(
            ValidationException.class,
            ()
                -> Task.builder()
                       .title("Implement login feature")
                       .description("Create login functionality using OAuth2")
                       .sprintBackLog(
                           TestSprintBackLogFactory.validSprintBackLog())
                       .assignee(TestUserFactory.validUser())
                       .estimatedHours(8.00)
                       .build());
    }

    @Test
    void souldFailToCreateTask_whenSprintBackLogIsNull() {
        assertThrows(
            ValidationException.class,
            ()
                -> Task.builder()
                       .title("Implement login feature")
                       .description("Create login functionality using OAuth2")
                       .userStory(TestUserStoryFactory.validUserStory())
                       .assignee(TestUserFactory.validUser())
                       .estimatedHours(8.00)
                       .build());
    }

    @Test
    void souldFailToCreateTask_whenAssigneeIsNull() {
        assertThrows(
            ValidationException.class,
            ()
                -> Task.builder()
                       .title("Implement login feature")
                       .description("Create login functionality using OAuth2")
                       .userStory(TestUserStoryFactory.validUserStory())
                       .sprintBackLog(
                           TestSprintBackLogFactory.validSprintBackLog())
                       .estimatedHours(8.00)
                       .build());
    }

    @ParameterizedTest
    @CsvSource({
        "updated title, Updated description, 12.5, ,true",
        "updated title, Updated description, 12.5, 10.0 ,true",
        "updated title, Updated description, 12.5, -10.0 ,false",
        "'', Updated description, 12.5, , false",
        "updated title, '', 12.5, , false",
        "updated title, Updated description, -5.0, , false",
    })
    void shouldValidateTaskUpdate(String title, String description,
                                  Double estimatedHours, Double actualHours,
                                  boolean expectedValidity) {
        Task task =
            Task.builder()
                .title("Initial Title")
                .description("Initial Description")
                .userStory(TestUserStoryFactory.validUserStory())
                .sprintBackLog(TestSprintBackLogFactory.validSprintBackLog())
                .assignee(TestUserFactory.validUser())
                .estimatedHours(8.00)
                .build();

        if (expectedValidity) {
            assertDoesNotThrow(() -> {
                task.updateMetadata(title, description, estimatedHours,
                                    actualHours);
            });
            if (title != null && !title.isEmpty())
                assert task.getTitle().equals(title);
            if (description != null && !description.isEmpty())
                assert task.getDescription().equals(description);
            if (estimatedHours != null)
                assert task.getEstimatedHours().equals(estimatedHours);
            if (actualHours != null)
                assert task.getActualHours().equals(actualHours);

        } else {
            assertThrows(ValidationException.class, () -> {
                task.updateMetadata(title, description, estimatedHours,
                                    actualHours);
            });
        }
    }
}
