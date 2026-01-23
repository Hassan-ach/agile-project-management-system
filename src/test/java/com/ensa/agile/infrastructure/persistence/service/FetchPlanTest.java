package com.ensa.agile.infrastructure.persistence.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class FetchPlanTest {

    @Test
    void resolve_ShouldSetSpecificFields_WhenRequested() {
        // Arrange: Request only SPRINTS and TASKS
        List<String> fields = List.of("SPRINTS", "TASKS");

        // Act
        FetchPlan plan = FetchPlan.resolve(fields);

        // Assert
        assertThat(plan.isSprints()).isTrue();

        // Verify others remain false
        assertThat(plan.isTasks()).isFalse(); // here tasks is false because we
                                              // didn't include user stories
        assertThat(plan.isProduct()).isFalse();
        assertThat(plan.isMembers()).isFalse();
        assertThat(plan.isEpics()).isFalse();
        assertThat(plan.isStories()).isFalse();
    }

    @Test
    void resolve_ShouldSetAllTrue_WhenAllKeywordIsPresent() {
        // Arrange: "ALL" (assuming FetchField.isAll checks for this string)
        List<String> fields = List.of("ALL");

        // Act
        FetchPlan plan = FetchPlan.resolve(fields);

        // Assert
        assertThat(plan)
            .extracting(FetchPlan::isProduct, FetchPlan::isMembers,
                        FetchPlan::isSprints, FetchPlan::isSprintMembers,
                        FetchPlan::isEpics, FetchPlan::isStories,
                        FetchPlan::isTasks)
            .containsOnly(true);
    }

    @Test
    void resolve_ShouldHandleCaseInsensitivity_IfSupported() {
        // Assuming FetchField.has() handles case insensitivity usually
        // If your FetchField logic is strict, remove this test.
        List<String> fields = List.of("sprints", "user_stories");

        FetchPlan plan = FetchPlan.resolve(fields);

        assertThat(plan.isSprints()).isTrue();
        assertThat(plan.isStories()).isTrue();
        assertThat(plan.isEpics()).isFalse();
    }
}
