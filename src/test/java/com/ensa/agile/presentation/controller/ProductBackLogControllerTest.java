package com.ensa.agile.presentation.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.agile.application.product.exception.ProductBackLogNotFoundException;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
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
class ProductBackLogControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AutenticationService auth; // Your helper service
    @Autowired private ProductBackLogRepository repo;

    private String accessToken;

    // IDs from SQL dump
    private final UUID existingProjectId =
        UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01");
    private final UUID devId =
        UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04"); // David

    @BeforeEach
    void setup() {
        // 1. Login as Admin or PO to get full access
        // Using "Sarah" (PO) as she likely has permissions for project
        // management
        accessToken = auth.login("sarah.po@techcorp.com", "password");
    }

    // =========================================================================
    //  CREATE
    // =========================================================================

    @Test
    void createProductBacklog_shouldReturnCreated() throws Exception {
        String jsonBody = """
            {
                "name": "New AI Project",
                "description": "Integration with Gemini"
            }
            """;

        mockMvc
            .perform(post("/api/v1/projects")
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.name").value("New AI Project"));
    }

    // =========================================================================
    //  READ
    // =========================================================================

    @Test
    void getProjectById_shouldReturnProject() throws Exception {
        mockMvc
            .perform(get("/api/v1/projects/{id}", existingProjectId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(existingProjectId.toString()))
            .andExpect(jsonPath("$.name").value("E-Commerce 2.0 Refactor"));
    }

    // =========================================================================
    //  UPDATE (PATCH)
    // =========================================================================

    @Test
    void updateProductBacklog_shouldReturnUpdatedInfo() throws Exception {
        String updateJson = """
            {
                "name": "E-Commerce 3.0"
            }
            """;

        mockMvc
            .perform(patch("/api/v1/projects/{id}", existingProjectId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("E-Commerce 3.0"));
    }

    // =========================================================================
    //  INVITE / REMOVE DEVELOPER
    // =========================================================================

    @Test
    void inviteDeveloper_shouldReturnCreated() throws Exception {
        String inviteJson = """
            {
                "email": "admin@techcorp.com" 
            }
            """;

        mockMvc
            .perform(post("/api/v1/projects/{id}/members/developers",
                          existingProjectId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(inviteJson))
            .andExpect(status().isCreated());
    }

    @Test
    void removeDeveloper_shouldReturnOk() throws Exception {
        mockMvc
            .perform(delete("/api/v1/projects/{id}/members/developers/{userId}",
                            existingProjectId, devId)
                         .header("Authorization", "Bearer " + accessToken)
                         .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    // =========================================================================
    //  DELETE PROJECT
    // =========================================================================

    @Test
    void deleteProjectById_shouldReturnNoContent() throws Exception {
        mockMvc
            .perform(delete("/api/v1/projects/{id}", existingProjectId)
                         .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNoContent());

        assertThrows(ProductBackLogNotFoundException.class,
                     () -> repo.findById(existingProjectId));
    }
}
