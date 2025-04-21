package com.bdserver.impactassist;

import com.bdserver.impactassist.model.JwtTokenDAO;
import com.bdserver.impactassist.model.RegisterLocalExpertDAO;
import com.bdserver.impactassist.model.RegisterUserDAO;
import com.bdserver.impactassist.model.ResponseLocalExpertDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestcontainersConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest {

    private String accessToken = "";
    private String refreshToken = "";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void userRegistration() throws Exception {
        RegisterUserDAO user = RegisterUserDAO.builder()
                .fullName("John")
                .email("john@example.com")
                .password("password")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        mockMvc.perform(get("/auth/users")).andExpect(status().isUnauthorized());
    }

    @Test
    @Order(2)
    public void userLogin() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@example.com\", \"password\":\"password\"}")
        ).andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        JwtTokenDAO token = objectMapper.readValue(json, JwtTokenDAO.class);
        accessToken = token.getAccessToken();
        refreshToken = token.getRefreshToken();
        assertEquals("USER", token.getRole());
        mockMvc.perform(get("/auth/users").header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void userRefresh() throws Exception {
        assertFalse(refreshToken.isEmpty());
        accessToken = "";
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        MvcResult result = mockMvc.perform(post("/auth/refresh").cookie(cookie)).andExpect(status().isOk()).andReturn();
        String newAccessToken = result.getResponse().getContentAsString();
        mockMvc.perform(get("/auth/users").header("Authorization", "Bearer " + newAccessToken)).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void localExpertRegistration() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@email.com\", \"password\":\"admin\"}")
        ).andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        JwtTokenDAO token = objectMapper.readValue(json, JwtTokenDAO.class);
        String adminAccessToken = token.getAccessToken();
        assertEquals("ADMIN", token.getRole());

        RegisterLocalExpertDAO user = RegisterLocalExpertDAO.builder()
                .fullName("Mike Thompson")
                .password("SecurePass123")
                .email("mike@thompsonsauto.com")
                .phone("+1-555-987-6543")
                .latitude(34.052235)
                .longitude(-118.243683)
                .description("Owner of Thompson's Auto Repair. ASE-certified with over 20 years of experience in engine diagnostics, brake systems, and transmission repair. Proudly serving the LA community with honest and reliable service.")
                .build();

        mockMvc.perform(post("/auth/local-expert")
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("4"));

        MvcResult resultLocalExperts = mockMvc.perform(get("/local_expert").header("Authorization", "Bearer " + adminAccessToken)).andExpect(status().isOk()).andReturn();
        String jsonLocalExperts = resultLocalExperts.getResponse().getContentAsString();
        List<ResponseLocalExpertDAO> response = objectMapper.readValue(jsonLocalExperts, new TypeReference<>() {
        });
        assertTrue(response.stream().anyMatch(localExpertResponse -> localExpertResponse.getId() == 4));
    }

    @Test
    @Order(5)
    public void loginBadRequest() throws Exception {
        mockMvc.perform(get("/auth/users")
                        .header("Authorization", "NotBearer " + accessToken)
                )
                .andExpect(status().isUnauthorized());
    }
}
