package com.wave.ds.auth.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wave.ds.auth.entity.Role;
import com.wave.ds.common.config.SecurityConfig;
import com.wave.ds.customer.controller.CustomerController;
import com.wave.ds.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/** Runs the real SecurityConfig and JwtService in front of a real controller. */
@WebMvcTest(CustomerController.class)
@Import({SecurityConfig.class, JwtService.class})
@TestPropertySource(properties = {
        "app.auth.jwt-secret=integration-test-secret-integration-1234",
        "app.auth.jwt-expiration-minutes=60",
        "app.auth.signup-enabled=true"})
class SecurityIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtService jwtService;

    @MockBean
    CustomerService customerService;

    @Test
    void requestWithoutTokenIsRejected() throws Exception {
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication required"));
    }

    @Test
    void requestWithInvalidTokenIsRejected() throws Exception {
        mockMvc.perform(get("/api/v1/customers").header("Authorization", "Bearer not.a.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void requestWithValidTokenIsAllowed() throws Exception {
        when(customerService.search(any(), any(), any(), any())).thenReturn(Page.empty());
        String token = jwtService.generate(1L, "p@example.com", Role.USER);

        mockMvc.perform(get("/api/v1/customers").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}
