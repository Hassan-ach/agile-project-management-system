package com.ensa.agile.presentation.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ensa.agile.application.user.request.AuthenticationRequest;
import com.ensa.agile.application.user.request.RefreshTokenRequest;
import com.ensa.agile.application.user.request.RegisterRequest;
import com.ensa.agile.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired UserRepository userRepo;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    // =========================================================================
    //  REGISTER TEST
    // =========================================================================

    @Test
    void shouldRegisterNewUserFlow() throws Exception {
        String uniqueEmail =
            "integration-test-" + UUID.randomUUID() + "@test.com";

        RegisterRequest request = new RegisterRequest(
            "New", "User", uniqueEmail, "password123", "password123");

        mockMvc
            .perform(post("/api/v1/auth/register")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accessToken", notNullValue()))
            .andExpect(jsonPath("$.refreshToken", notNullValue()));
    }

    // =========================================================================
    //  LOGIN TEST
    // =========================================================================

    @Test
    void shouldLoginExistingUserFlow() throws Exception {
        // Using a user verified to exist in your SQL dump (Sarah - PO)
        AuthenticationRequest request =
            new AuthenticationRequest("sarah.po@techcorp.com", "password");

        mockMvc
            .perform(post("/api/v1/auth/login")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken", notNullValue()))
            .andExpect(jsonPath("$.refreshToken", notNullValue()));
    }

    // =========================================================================
    //  GET USER INFO (ME) TEST
    // =========================================================================

    @Test
    void shouldGetUserInfoFlow() throws Exception {
        // 1. Login first to get a valid Token
        String token = loginAndGetToken("sarah.po@techcorp.com", "password");

        // 2. Use Token to call /me
        mockMvc
            .perform(get("/api/v1/auth/me")
                         .header("Authorization", "Bearer " + token)
                         .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("sarah.po@techcorp.com"))
            .andExpect(jsonPath("$.firstName").value("Sarah"))
            .andExpect(jsonPath("$.lastName").value("Jenkins"));
    }

    // =========================================================================
    //  REFRESH TOKEN TEST
    // =========================================================================

    @Test
    void shouldRefreshTokenFlow() throws Exception {
        // 1. Login to get the Refresh Token
        AuthenticationRequest loginReq =
            new AuthenticationRequest("sarah.po@techcorp.com", "password");

        String loginResponse =
            mockMvc
                .perform(
                    post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(loginResponse);
        String refreshToken = jsonNode.get("refreshToken").asText();

        // 2. Perform Refresh
        RefreshTokenRequest refreshReq = new RefreshTokenRequest(refreshToken);

        mockMvc
            .perform(post("/api/v1/auth/refresh")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(refreshReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken", notNullValue()));
        // Note: Depending on logic, refresh token might or might not rotate
    }

    // =========================================================================
    //  HELPER
    // =========================================================================

    private String loginAndGetToken(String email, String password)
        throws Exception {
        AuthenticationRequest request =
            new AuthenticationRequest(email, password);

        String response =
            mockMvc
                .perform(post("/api/v1/auth/login")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode root = objectMapper.readTree(response);
        return root.get("accessToken").asText();
    }
}
