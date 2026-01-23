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

import com.ensa.agile.domain.task.repository.TaskRepository;
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
class TaskControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AutenticationService auth;

    private String accessToken;

    // IDs from your SQL dump
    private final UUID storyId = UUID.fromString(
        "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01"); // "User Login API"
    private final UUID existingTaskId = UUID.fromString(
        "f0eebc99-9c0b-4ef8-bb6d-6bb9bd380f01"); // "Backend Login Logic"

    @BeforeEach
    void setup() {

        accessToken = auth.login("david.dev@techcorp.com", "password");
    }

    // =========================================================================
    //  CREATE TASK
    // =========================================================================

    @Test
    void createTask_shouldReturnCreated() throws Exception {
        String jsonBody = """
            {
                "title": "Unit Tests for Login",
                "description": "Write JUnit 5 tests",
                "estimatedHours": 3.5
            }
            """;

        mockMvc
            .perform(post("/api/v1/stories/{storyId}/tasks", storyId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.title").value("Unit Tests for Login"));
    }

    // =========================================================================
    //  GET TASK
    // =========================================================================

    @Test
    void getTask_shouldReturnTaskDetails() throws Exception {
        mockMvc
            .perform(get("/api/v1/tasks/{id}", existingTaskId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(existingTaskId.toString()))
            .andExpect(jsonPath("$.title").value("Backend Login Logic"));
    }

    // =========================================================================
    //  UPDATE TASK (Details)
    // =========================================================================

    @Test
    void updateTask_shouldReturnUpdatedInfo() throws Exception {
        // must be scrum master or product owner to update task details (and
        // sprint member)
        accessToken = auth.login("mike.sm@techcorp.com", "password");
        String updateJson = """
            {
                "title": "Backend Login Logic (Refactored)",
                "description": "Updated desc",
                "estimatedHours": 5.0
            }
            """;

        mockMvc
            .perform(put("/api/v1/tasks/{id}", existingTaskId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.title").value("Backend Login Logic (Refactored)"))
            .andExpect(jsonPath("$.estimatedHours").value(5.0));
    }

    // =========================================================================
    //  ASSIGN / UNASSIGN
    // =========================================================================

    @Test
    void assignTask_shouldUpdateAssignee() throws Exception {
        // Assign to Emily (another dev)
        String assignJson = """
            {
                "assigneeEmail": "emily.dev@techcorp.com"
            }
            """;

        // existing Task id that not assigned from SQL dump
        var taskId = UUID.fromString("f0eebc99-9c0b-4ef8-bb6d-6bb9bd380f03");

        mockMvc
            .perform(patch("/api/v1/tasks/{id}/assignee", taskId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(assignJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.assigned").value(true))
            .andExpect(
                jsonPath("$.task.assignee").value("emily.dev@techcorp.com"));
    }

    @Test
    void unAssignTask_shouldRemoveAssignee() throws Exception {
        String unAssignJson = """
            {
                "assigneeEmail": "david.dev@techcorp.com"
            }
            """;

        mockMvc
            .perform(delete("/api/v1/tasks/{id}/assignee", existingTaskId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(unAssignJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.task.assignee").isEmpty());
    }

    // =========================================================================
    //  UPDATE STATUS (Workflow)
    // =========================================================================

    @Test
    void updateTaskStatus_shouldChangeStatusAndLogHistory() throws Exception {
        String statusJson = """
            {
                "status": "IN_TEST",
                "note": "Code review passed"
            }
            """;

        mockMvc
            .perform(patch("/api/v1/tasks/{id}/status", existingTaskId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(statusJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.updated").value(true))
            .andExpect(jsonPath("$.status.status").value("IN_TEST"));
    }

    // =========================================================================
    //  DELETE TASK
    // =========================================================================

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {
        // must be scrum master or product owner to delete task (and
        // sprint member)
        accessToken = auth.login("mike.sm@techcorp.com", "password");
        mockMvc
            .perform(delete("/api/v1/tasks/{id}", existingTaskId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNoContent());

        mockMvc
            .perform(get("/api/v1/tasks/{id}", existingTaskId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNotFound()); // Expecting 404 from UseCase
    }

    // =========================================================================
    //  LIST TASKS
    // =========================================================================

    @Test
    void getAllTasks_shouldReturnTasksForStory() throws Exception {
        mockMvc
            .perform(get("/api/v1/stories/{storyId}/tasks", storyId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath(
                "$", hasSize(3))); // SQL dump has 3 tasks for this story
    }

    @Test
    void getMyTasks_shouldReturnAssignedTasks() throws Exception {
        mockMvc
            .perform(get("/api/v1/tasks/me")
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }
}
