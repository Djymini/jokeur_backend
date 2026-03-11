package com.ashleydev.jokeur_api.e2e;

import com.ashleydev.jokeur_api.e2e.config.TestContainerConfig;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
public class UserAppointmentE2ETest extends TestContainerConfig {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;

    private String jwt;
    private String userId;

    @BeforeEach
    void cleanAndSetup() throws Exception {
        userRepository.deleteAll();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "username": "John",
                                        "email": "test@test.com",
                                        "password": "P@ssword1234",
                                        "firstname": "John",
                                        "name": "Doe"
                                    }
                                """))
                .andExpect(status().isOk());

        String authResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "email": "test@test.com", "password": "P@ssword1234" }
                                """))
                .andReturn().getResponse().getContentAsString();

        JSONObject authObj = new JSONObject(authResponse);
        this.jwt = authObj.getString("token");
        this.userId = authObj.getString("id");
    }

    @Test
    void shouldCreateAndRetrieveVariousAppointments() throws Exception {
        String jwt = this.jwt;
        String userId = this.userId;

        mockMvc.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "reason": "Rendez-vous Vétérinaire",
                                    "dateTime": "2026-03-11T14:47",
                                    "userId": %s
                                }
                            """.formatted(userId)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldModifyExistingMeasure() throws Exception {
        String jwt = this.jwt;
        String userId = this.userId;

        String treatmentRes = mockMvc.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "reason": "Rendez-vous Vétérinaire",
                                    "dateTime": "2026-03-11T14:47",
                                    "userId": %s
                                }
                            """.formatted(userId)))
                .andReturn().getResponse().getContentAsString();

        String appointmentId = new JSONObject(treatmentRes).getString("id");

        mockMvc.perform(put("/appointment/" + userId + "/" + appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "reason": "Rendez-vous changé",
                                    "dateTime": "2026-03-11T14:47",
                                    "userId": %s
                                }
                            """.formatted(userId)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteTreatment() throws Exception {
        String jwt = this.jwt;
        String userId = this.userId;

        String appointmentRes = mockMvc.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "reason": "Rendez-vous Vétérinaire",
                                    "dateTime": "2026-03-11T14:47",
                                    "userId": %s
                                }
                            """.formatted(userId)))
                .andReturn().getResponse().getContentAsString();

        String appointmentId = new JSONObject(appointmentRes).getString("id");

        mockMvc.perform(delete("/appointment/" + userId + "/" + appointmentId)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }
}
