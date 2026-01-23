package com.ensa.agile.infrastructure.persistence.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ensa.agile.application.epic.request.EpicGetRequest;
import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.product.request.ProductBackLogGetRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.application.sprint.request.SprintBackLogGetRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.story.request.UserStoryGetRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.domain.story.enums.MoscowType;
import com.ensa.agile.domain.story.enums.StoryStatus;
import com.ensa.agile.domain.task.enums.TaskStatus;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(FetchService.class)
@Transactional
class FetchServiceTest {

    @Autowired private FetchService fetchService;

    // UUIDs matching /test-data.sql
    private final UUID productId =
        UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01");
    private final UUID sprintId =
        UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01");
    private final UUID epicId =
        UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c01");
    private final UUID storyId =
        UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01");

    // =========================================================================
    //  PRODUCT FETCH TESTS
    // =========================================================================

    @Test
    void testGetProductResponse_FullFetch() {
        // Arrange
        ProductBackLogGetRequest req = new ProductBackLogGetRequest(
            productId, "SPRINTS,EPICS,USER_STORIES,TASKS,MEMBERS");

        // Act
        ProductBackLogResponse response = fetchService.getResponse(req);

        // Assert Root
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("E-Commerce 2.0 Refactor");
        assertThat(response.getCreatedBy())
            .isEqualTo(UUID.fromString(
                "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01")); // Admin

        // Assert Sprints
        assertThat(response.getSprintBackLogs()).hasSize(1);
        var sprint = response.getSprintBackLogs().get(0);
        assertThat(sprint.getName()).isEqualTo("Sprint 1 - Foundation");
        assertThat(sprint.getStatus()).isEqualTo(SprintStatus.ACTIVE);

        // Assert Epics
        assertThat(response.getEpics()).hasSize(2);
        var epic =
            response.getEpics()
                .stream()
                .filter(e -> e.getTitle().equals("Authentication Module"))
                .findFirst()
                .orElseThrow();

        // Assert Stories (Nested in Epic)
        assertThat(epic.getUserStories()).isNotEmpty();
        var story = epic.getUserStories().get(0);
        assertThat(story.getTitle()).isEqualTo("User Login API");
        assertThat(story.getStatus()).isEqualTo(StoryStatus.IN_PROGRESS);

        // Assert Tasks (Nested in Story)
        assertThat(story.getTasks()).hasSize(3);
        var task = story.getTasks()
                       .stream()
                       .filter(t -> t.getTitle().equals("Backend Login Logic"))
                       .findFirst()
                       .orElseThrow();
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getAssignee()).isEqualTo("david.dev@techcorp.com");

        // Assert Members
        assertThat(response.getProjectMembers()).hasSize(4); // PO, SM, 2 Devs
        boolean hasDavid = response.getProjectMembers().stream().anyMatch(
            m
            -> m.getUserEmail().equals("david.dev@techcorp.com") &&
                   m.getRole() == RoleType.DEVELOPER);
        assertThat(hasDavid).isTrue();
    }

    // =========================================================================
    //  SPRINT FETCH TESTS
    // =========================================================================

    @Test
    void testGetSprintResponse_FullFetch() {
        // Arrange
        SprintBackLogGetRequest req = new SprintBackLogGetRequest(
            sprintId, "SPRINTS_MEMBERS, USER_STORIES, TASKS");

        // Act
        SprintBackLogResponse response = fetchService.getResponse(req);

        // Assert Root
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Sprint 1 - Foundation");
        assertThat(response.getScrumMasterEmail())
            .isEqualTo("mike.sm@techcorp.com");

        // Assert Members (Sprint Members)
        assertThat(response.getMembers()).hasSize(3); // David & Emily & mike
        assertThat(response.getMembers().stream().anyMatch(
                       m -> m.getUserEmail().equals("emily.dev@techcorp.com")))
            .isTrue();

        // Assert Stories
        assertThat(response.getUserStories()).hasSize(2); // Login & Add to Cart
        var story = response.getUserStories()
                        .stream()
                        .filter(s -> s.getTitle().equals("User Login API"))
                        .findFirst()
                        .orElseThrow();

        // Assert Tasks Stitching
        assertThat(story.getTasks()).hasSize(3);
        assertThat(story.getTasks().stream().anyMatch(
                       t -> t.getTitle().equals("Backend Login Logic")))
            .isTrue();
    }

    // =========================================================================
    //  EPIC FETCH TESTS
    // =========================================================================

    @Test
    void testGetEpicResponse_FullFetch() {
        // Arrange
        EpicGetRequest req = new EpicGetRequest(epicId, "USER_STORIES,TASKS");

        // Act
        EpicResponse response = fetchService.getResponse(req);

        // Assert Root
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Authentication Module");

        // Assert Stories
        assertThat(response.getUserStories())
            .hasSize(2); // Login (Sprint 1) & Password Reset (Backlog)

        // Assert Story Content
        var story = response.getUserStories()
                        .stream()
                        .filter(s -> s.getTitle().equals("Password Reset Flow"))
                        .findFirst()
                        .orElseThrow();
        assertThat(story.getPriority()).isEqualTo(MoscowType.COULD_HAVE);

        // Assert Tasks exist for Login Story
        var loginStory = response.getUserStories()
                             .stream()
                             .filter(s -> s.getTitle().equals("User Login API"))
                             .findFirst()
                             .orElseThrow();
        assertThat(loginStory.getTasks()).isNotEmpty();
    }

    // =========================================================================
    //  USER STORY FETCH TESTS
    // =========================================================================

    @Test
    void testGetUserStoryResponse() {
        // Arrange
        UserStoryGetRequest req = new UserStoryGetRequest(storyId, "TASK");

        // Act
        UserStoryResponse response = fetchService.getResponse(req);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("User Login API");
        assertThat(response.getStatus()).isEqualTo(StoryStatus.IN_PROGRESS);
        assertThat(response.getStoryPoints()).isEqualTo(5);

        // Assert Tasks are always fetched for single story view
        assertThat(response.getTasks()).hasSize(3);
        assertThat(response.getTasks().stream().anyMatch(
                       t -> t.getTitle().equals("Frontend Login Form")))
            .isTrue();
    }
}
