package com.ensa.agile.infrastructure.persistence.service;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class BackLogRepoTest {

    @Autowired private BackLogRepo backLogRepo;

    // UUIDs match the SQL insert statements
    private final UUID productId = UUID.fromString(
        "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01"); // E-Commerce 2.0
    private final UUID sprintId =
        UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01"); // Sprint 1
    private final UUID epicId =
        UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c01"); // Auth Module
    private final UUID storyId = UUID.fromString(
        "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01"); // User Login API

    @BeforeEach
    void setup() {
        // Setup logic if needed
    }

    // =========================================================================
    //  SECTION 1: PRODUCT FETCH TESTS
    // =========================================================================

    @Test
    void testFindProductHeader() {
        var result = backLogRepo.findProductHeader(productId);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("E-Commerce 2.0 Refactor");
    }

    @Test
    void testFindSprintsByProduct() {
        var results = backLogRepo.findSprintsByProduct(productId);
        assertThat(results).isNotEmpty();

        var sprint = results.get(0);
        assertThat(sprint.getName()).isEqualTo("Sprint 1 - Foundation");
        // SQL ID ...a03 is mike.sm@techcorp.com
        assertThat(sprint.getScrumMasterEmail())
            .isEqualTo("mike.sm@techcorp.com");
        // SQL Sprint History sets status to ACTIVE
        assertThat(sprint.getLatestStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void testFindEpicsByProduct() {
        var results = backLogRepo.findEpicsByProduct(productId);
        assertThat(results).isNotEmpty();
        // SQL Title for ...c01
        assertThat(results.get(0).getTitle())
            .isEqualTo("Authentication Module");
    }

    @Test
    void testFindMembersByProduct() {
        var results = backLogRepo.findMembersByProduct(productId);
        assertThat(results).isNotEmpty();

        // We look for David Dev specifically or just check the list contents
        // Assuming the query returns list including David:
        boolean davidFound = results.stream().anyMatch(
            m
            -> m.getEmail().equals("david.dev@techcorp.com") &&
                   m.getRole().equals("DEVELOPER"));

        assertThat(davidFound).isTrue();
    }

    @Test
    void testFindStoriesByProduct() {
        var results = backLogRepo.findStoriesByProduct(productId);
        assertThat(results).isNotEmpty();

        // Find specific story ...e01
        var story = results.stream()
                        .filter(s -> s.getTitle().equals("User Login API"))
                        .findFirst()
                        .orElseThrow();

        assertThat(story.getSprintId()).isEqualTo(sprintId);
        // SQL User Story History sets status to IN_PROGRESS
        assertThat(story.getLatestStatus()).isEqualTo("IN_PROGRESS");
    }

    @Test
    void testFindTasksByProduct() {
        var results = backLogRepo.findTasksByProduct(productId);
        assertThat(results).isNotEmpty();

        var task = results.stream()
                       .filter(t -> t.getTitle().equals("Backend Login Logic"))
                       .findFirst()
                       .orElseThrow();

        // SQL Assignee for ...f01 is ...a04 (David Dev)
        assertThat(task.getAssigneeEmail()).isEqualTo("david.dev@techcorp.com");
        // SQL Task History sets status to IN_PROGRESS
        assertThat(task.getLatestStatus()).isEqualTo("IN_PROGRESS");
    }

    // =========================================================================
    //  SECTION 2: SPRINT FETCH TESTS
    // =========================================================================

    @Test
    void testFindSprintHeader() {
        var result = backLogRepo.findSprintHeader(sprintId);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Sprint 1 - Foundation");
        // Corrected email based on SQL ID ...a03
        assertThat(result.getScrumMasterEmail())
            .isEqualTo("mike.sm@techcorp.com");
    }

    @Test
    void testFindMembersBySprint() {
        var results = backLogRepo.findMembersBySprint(sprintId);
        assertThat(results).isNotEmpty();
        // SQL inserts David Dev (...a04) and Emily Dev (...a05) into Sprint
        // Members
        boolean memberFound = results.stream().anyMatch(
            m -> m.getUserEmail().equals("david.dev@techcorp.com"));
        assertThat(memberFound).isTrue();
    }

    @Test
    void testFindStoriesBySprint() {
        var results = backLogRepo.findStoriesBySprint(sprintId);
        assertThat(results).isNotEmpty();
        // Corrected title based on SQL ID ...e01
        assertThat(results.get(0).getTitle()).isEqualTo("User Login API");
    }

    @Test
    void testFindTasksBySprint() {
        var results = backLogRepo.findTasksBySprint(sprintId);
        assertThat(results).isNotEmpty();
        // Corrected title based on SQL ID ...f01
        boolean taskFound = results.stream().anyMatch(
            t -> t.getTitle().equals("Backend Login Logic"));
        assertThat(taskFound).isTrue();
    }

    // =========================================================================
    //  SECTION 3: EPIC FETCH TESTS
    // =========================================================================

    @Test
    void testFindEpicHeader() {
        var result = backLogRepo.findEpicHeader(epicId);
        assertThat(result).isNotNull();
        // Corrected title based on SQL ID ...c01
        assertThat(result.getTitle()).isEqualTo("Authentication Module");
    }

    @Test
    void testFindStoriesByEpic() {
        var results = backLogRepo.findStoriesByEpic(epicId);
        assertThat(results).isNotEmpty();
        // Corrected title based on SQL ID ...e01
        assertThat(results.get(0).getTitle()).isEqualTo("User Login API");
    }

    @Test
    void testFindTasksByEpic() {
        var results = backLogRepo.findTasksByEpic(epicId);
        assertThat(results).isNotEmpty();
        // Ensure the task belongs to the story that belongs to the epic
        assertThat(results.get(0).getUserStoryId()).isEqualTo(storyId);
    }

    // =========================================================================
    //  SECTION 4: STORY FETCH TESTS
    // =========================================================================

    @Test
    void testFindStoryHeader() {
        var result = backLogRepo.findStoryHeader(storyId);
        assertThat(result).isNotNull();
        // Corrected title based on SQL ID ...e01
        assertThat(result.getTitle()).isEqualTo("User Login API");
        assertThat(result.getLatestStatus()).isEqualTo("IN_PROGRESS");
    }

    @Test
    void testFindTasksByStory() {
        var results = backLogRepo.findTasksByStory(storyId);
        assertThat(results).isNotEmpty();

        var task = results.get(0);
        // Corrected title based on SQL ID ...f01
        assertThat(task.getTitle()).isEqualTo("Backend Login Logic");
        // Corrected assignee based on SQL ID ...a04
        assertThat(task.getAssigneeEmail()).isEqualTo("david.dev@techcorp.com");
    }
}
