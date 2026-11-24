package com.wave.ds.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wave.ds.auth.dto.AuthResponse;
import com.wave.ds.auth.dto.UserResponse;
import com.wave.ds.auth.entity.Role;
import com.wave.ds.auth.service.AuthService;
import com.wave.ds.common.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService service;

    @Test
    void signup_returns201WithToken() throws Exception {
        when(service.signup(any())).thenReturn(new AuthResponse("tok", "Bearer", 3600,
                new UserResponse(1L, "Pravar", "p@example.com", Role.ADMIN)));

        mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON).content("""
                        {"fullName":"Pravar","email":"p@example.com","password":"Secret123"}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("tok"))
                .andExpect(jsonPath("$.user.role").value("ADMIN"));
    }

    @Test
    void signup_rejectsWeakPasswordAndBadEmail() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON).content("""
                        {"fullName":"Pravar","email":"not-an-email","password":"short"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists())
                .andExpect(jsonPath("$.fieldErrors.password").exists());
    }

    @Test
    void signup_rejectsPasswordWithoutDigit() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON).content("""
                        {"fullName":"Pravar","email":"p@example.com","password":"onlyletters"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.password")
                        .value("Password must contain at least one letter and one number"));
    }

    @Test
    void login_returns401ForBadCredentials() throws Exception {
        when(service.login(any())).thenThrow(new UnauthorizedException("Invalid email or password"));

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                        {"email":"p@example.com","password":"Whatever1"}"""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }
}
