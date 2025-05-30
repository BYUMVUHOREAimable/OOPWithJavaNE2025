package rw.gov.payroll.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Tests for the AuthController class.
 * These tests verify the authentication endpoints functionality.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test for the login endpoint with invalid credentials.
     * This test verifies that the login endpoint returns 401 Unauthorized
     * when invalid credentials are provided.
     */
    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        String requestBody = "{"
                + "\"email\": \"nonexistent@example.com\","
                + "\"password\": \"wrongpassword\""
                + "}";

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test for the register endpoint with invalid data.
     * This test verifies that the register endpoint returns 400 Bad Request
     * when invalid registration data is provided.
     */
    @Test
    public void testRegisterWithInvalidData() throws Exception {
        String requestBody = "{"
                + "\"email\": \"\","  // Empty email
                + "\"password\": \"password\","
                + "\"firstName\": \"Test\","
                + "\"lastName\": \"User\""
                + "}";

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}