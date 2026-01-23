package com.ensa.agile.presentation.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.agile.application.epic.exception.EpicNotFoundException;
import com.ensa.agile.domain.epic.repository.EpicRepository;
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
class EpicControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private AutenticationService auth;

    @Autowired private EpicRepository repo;
    private String accessToken;

    private final UUID productId =
        UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01");
    private final UUID epicId = UUID.fromString(
        "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c01"); // "Authentication Module"

    @BeforeEach
    void setup() {
        // 1.login to get access token
        // when(abac.canAccessEpic(any(), any(), any())).thenReturn(true);
        accessToken = auth.login("sarah.po@techcorp.com", "password");
    }

    // =========================================================================
    //  CREATE EPIC TESTS
    // =========================================================================

    @Test
    void createEpic_shouldReturnCreated_whenValidRequest() throws Exception {

        String jsonBody = """
            {
                "title": "Payment Integration",
                "description": "Handle Stripe payments"
            }
            """;

        mockMvc
            .perform(post("/api/v1/projects/{projectId}/epics", productId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.title").value("Payment Integration"));
    }

    // =========================================================================
    //  GET ALL EPICS TESTS
    // =========================================================================

    @Test
    void getAllEpics_shouldReturnList_whenProjectExists() throws Exception {
        mockMvc
            .perform(get("/api/v1/projects/{projectId}/epics", productId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$", hasSize(2))) // Auth Module & Cart Management
            .andExpect(jsonPath("$[0].title", notNullValue()));
    }

    // =========================================================================
    //  GET EPIC BY ID TESTS
    // =========================================================================

    @Test
    void getEpicById_shouldReturnEpic_whenExists() throws Exception {
        mockMvc
            .perform(get("/api/v1/epics/{id}", epicId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(epicId.toString()))
            .andExpect(jsonPath("$.title").value("Authentication Module"));
    }

    // =========================================================================
    //  UPDATE EPIC TESTS
    // =========================================================================

    @Test
    void updateEpic_shouldReturnUpdatedEpic_whenValid() throws Exception {
        String updateJson = """
            {
                "title": "Updated Auth Module",
                "description": "Updated Description"
            }
            """;

        mockMvc
            .perform(put("/api/v1/epics/{id}", epicId)
                         .contentType(MediaType.APPLICATION_JSON)
                         .header("Authorization", "Bearer " + accessToken)
                         .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Updated Auth Module"));
    }

    // =========================================================================
    //  DELETE EPIC TESTS
    // =========================================================================

    @Test
    void deleteEpic_shouldReturnNoContent_whenSuccessful() throws Exception {
        mockMvc
            .perform(delete("/api/v1/epics/{id}", epicId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNoContent());

        assertThrows(EpicNotFoundException.class, () -> repo.findById(epicId));
    }
}
