package com.ensa.agile.presentation.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.agile.infrastructure.security.service.AutenticationService;
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
class UserStoryControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AutenticationService auth;

    private String accessToken;

    // IDs from your SQL dump
    private final UUID projectId =
        UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01");
    private final UUID storyId = UUID.fromString(
        "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01"); // "User Login API"
    private UUID epicId = UUID.fromString(
        "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c01"); // "Authentication Module"
    private final UUID ownerId =
        UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02"); // Sarah PO

    @BeforeEach
    void setup() {
        accessToken = auth.login("sarah.po@techcorp.com", "password");
    }

    // =========================================================================
    //  CREATE
    // =========================================================================

    @Test
    void createUserStory_shouldReturnCreated() throws Exception {
        String jsonBody = """
            {
                "title": "Integration with PayPal",
                "description": "Allow users to pay via PayPal",
                "priority": "MUST_HAVE",
                "storyPoints": 5,
                "acceptanceCriteria": "Users can select PayPal as a payment option during checkout."
            }
            """;

        mockMvc
            .perform(post("/api/v1/projects/{projectId}/stories", projectId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.title").value("Integration with PayPal"));
    }

    // =========================================================================
    //  GET
    // =========================================================================

    @Test
    void getUserStoryById_shouldReturnDetails() throws Exception {
        mockMvc
            .perform(get("/api/v1/stories/{id}", storyId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(storyId.toString()))
            .andExpect(jsonPath("$.title").value("User Login API"));
    }

    // =========================================================================
    //  UPDATE
    // =========================================================================

    @Test
    void updateUserStory_shouldReturnUpdatedDetails() throws Exception {
        String updateJson = """
            {
                "title": "User Login API (v2)",
                "description": "Updated Description",
                "storyPoints": 8
            }
            """;

        mockMvc
            .perform(put("/api/v1/stories/{id}", storyId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("User Login API (v2)"))
            .andExpect(jsonPath("$.storyPoints").value(8));
    }

    // =========================================================================
    //  UPDATE STATUS
    // =========================================================================

    @Test
    void updateUserStoryStatus_shouldUpdateStatus() throws Exception {
        accessToken = auth.login("mike.sm@techcorp.com", "password");

        String statusJson = """
            {
                "status": "DONE",
                "note": "QA Passed"
            }
            """;

        mockMvc
            .perform(patch("/api/v1/stories/{id}/status", storyId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(statusJson))
            .andExpect(status().isOk())
            // Assuming UpdateStatusResponse wrapper pattern:
            .andExpect(jsonPath("$.status.status").value("DONE"));
    }

    // =========================================================================
    //  LINK / UNLINK EPIC
    // =========================================================================

    @Test
    void linkUserStoryToEpic_shouldReturnOk() throws Exception {
        // We can re-link the same epic or a different one.
        // This tests the endpoint connectivity.
        epicId = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c02");
        mockMvc
            .perform(
                patch("/api/v1/stories/{id}/epic/{epicId}", storyId, epicId)
                    .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk());
    }

    @Test
    void unLinkUserStoryToEpic_shouldReturnOk() throws Exception {
        mockMvc
            .perform(
                delete("/api/v1/stories/{id}/epic/{epicId}", storyId, epicId)
                    .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk());
    }

    // =========================================================================
    //  UPDATE PRIORITY
    // =========================================================================

    @Test
    void updateStoryPriority_shouldReturnOk() throws Exception {
        String priorityJson = """
            {
                "priority": "SHOULD_HAVE"
            }
            """;

        mockMvc
            .perform(patch("/api/v1/stories/{id}/priority", storyId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(priorityJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.priority").value("SHOULD_HAVE"))
            .andExpect(jsonPath("$.id").value(storyId.toString()));
    }

    // =========================================================================
    //  GET HISTORY
    // =========================================================================

    @Test
    void getUserStoryHistory_shouldReturnList() throws Exception {
        mockMvc
            .perform(get("/api/v1/stories/{id}/history", storyId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].status").value("IN_PROGRESS"));
    }

    // =========================================================================
    //  DELETE
    // =========================================================================

    @Test
    void deleteUserStory_shouldReturnNoContent() throws Exception {
        mockMvc
            .perform(delete("/api/v1/stories/{id}", storyId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNoContent());

        mockMvc
            .perform(get("/api/v1/stories/{id}", storyId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNotFound());
    }
}
