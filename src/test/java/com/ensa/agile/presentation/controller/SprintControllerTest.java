package com.ensa.agile.presentation.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.agile.application.sprint.exception.SprintBackLogNotFoundException;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.JpaUserStoryRepository;
import com.ensa.agile.infrastructure.security.service.AutenticationService;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SprintControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AutenticationService auth;
    @Autowired private SprintBackLogRepository repo;
    @Autowired private JpaUserStoryRepository storyRepo;

    private String accessToken;

    // IDs from SQL dump
    private final UUID projectId =
        UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01");
    private final UUID sprintId =
        UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01");

    // Story currently in Sprint 1 (for Remove test)
    private final UUID storyInSprintId =
        UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01");
    // Story currently in Product Backlog (for Add test)
    private final UUID storyInBacklogId =
        UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e03");

    @BeforeEach
    void setup() {
        // Login as Mike (Scrum Master) who created the sprint in the DB dump
        accessToken = auth.login("sarah.po@techcorp.com", "password");
    }

    // =========================================================================
    //  CREATE
    // =========================================================================

    @Test
    void createSprint_shouldReturnCreated() throws Exception {
        String jsonBody = String.format("""
            {
                "name": "Sprint 2 - Core Features",
                "goal": "Implement Shopping Cart",
                "startDate": "%s",
                "endDate": "%s",
                "scrumMasterEmail": "mike.sm@techcorp.com"
            }
            """, LocalDate.now().plusDays(1),
                                        LocalDate.now().plusDays(15));

        mockMvc
            .perform(post("/api/v1/projects/{projectId}/sprints", projectId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.name").value("Sprint 2 - Core Features"));
    }

    // =========================================================================
    //  READ
    // =========================================================================

    @Test
    void getSprintById_shouldReturnSprint() throws Exception {
        mockMvc
            .perform(get("/api/v1/sprints/{id}", sprintId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sprintId.toString()))
            .andExpect(jsonPath("$.name").value("Sprint 1 - Foundation"));
    }

    @Test
    void getSprintHistory_shouldReturnList() throws Exception {
        mockMvc
            .perform(get("/api/v1/sprints/{id}/history", sprintId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    // =========================================================================
    //  UPDATE INFO
    // =========================================================================

    @Test
    void updateSprint_shouldReturnUpdatedInfo() throws Exception {
        String updateJson = """
            {
                "name": "Sprint 1 - Foundation (Revised)",
                "goal": "Updated Goal"
            }
            """;

        mockMvc
            .perform(put("/api/v1/sprints/{id}", sprintId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.name").value("Sprint 1 - Foundation (Revised)"));
    }

    // =========================================================================
    //  UPDATE STATUS (PATCH)
    // =========================================================================

    @Test
    void updateSprintStatus_shouldReturnUpdatedStatus() throws Exception {
        accessToken = auth.login("mike.sm@techcorp.com", "password");
        String statusJson = """
            {
                "status": "COMPLETED",
                "note": "All goals met"
            }
            """;

        mockMvc
            .perform(patch("/api/v1/sprints/{id}/status", sprintId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(statusJson))
            .andExpect(status().isOk());
        // .andExpect(
        //     jsonPath("$.status")
        //         .value("COMPLETED")); // Assuming response structure matches
    }

    // =========================================================================
    //  MANAGE STORIES
    // =========================================================================

    @Test
    void addStoryToSprint_shouldReturnOk() throws Exception {
        // Adding 'Password Reset Flow' (currently in Backlog) to Sprint 1
        mockMvc
            .perform(post("/api/v1/sprints/{id}/stories/{storyId}", sprintId,
                          storyInBacklogId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk());
        // You could verify JSON path if your use case returns the updated list
    }

    @Test
    void removeStoryFromSprint_shouldReturnOk() throws Exception {
        // Removing 'User Login API' (currently in Sprint 1)
        mockMvc
            .perform(delete("/api/v1/sprints/{id}/stories/{storyId}", sprintId,
                            storyInSprintId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk());

        var removedStory = storyRepo.findById(storyInSprintId).orElseThrow();
        assertNull(removedStory.getSprintBackLog());
    }

    // =========================================================================
    //  DELETE
    // =========================================================================

    @Test
    void deleteSprint_shouldReturnNoContent() throws Exception {
        mockMvc
            .perform(delete("/api/v1/sprints/{id}", sprintId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNoContent());

        assertThrows(SprintBackLogNotFoundException.class,
                     () -> repo.findById(sprintId));
    }
}
