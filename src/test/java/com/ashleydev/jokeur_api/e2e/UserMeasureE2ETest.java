package com.ashleydev.jokeur_api.e2e;

import com.ashleydev.jokeur_api.e2e.config.TestContainerConfig;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
public class UserMeasureE2ETest extends TestContainerConfig {
    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;

    private String jwt;
    private String hrId;

    @BeforeEach
    void cleanAndSetup() throws Exception {
        healthRecordRepository.deleteAll();
        userRepository.deleteAll();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "Marc",
                        "email": "test2@test.com",
                        "password": "P@ssword1234",
                        "firstname": "Marc",
                        "name": "Doe"
                    }
                    """)).andExpect(status().isOk());

        String authResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "email": "test2@test.com", "password": "P@ssword1234" }
                                """))
                .andReturn().getResponse().getContentAsString();

        JSONObject authObj = new JSONObject(authResponse);
        this.jwt = authObj.getString("token");
        String userId = authObj.getString("id");

        String hrResponse = mockMvc.perform(post("/health-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "userId": %s,
                                    "petName": "Felix",
                                    "animalType": "CAT",
                                    "breed": "SIAMESE",
                                    "sex": "FEMALE",
                                    "birthDate": "2025-02-27",
                                    "currentWeight": 15.0,
                                    "color": "BROWN",
                                    "identificationNumber": "hin55ykondpoCLI",
                                    "tattoo": "ABC101",
                                    "allergy": "Aucune"
                                }
                                """.formatted(userId)))
                .andReturn().getResponse().getContentAsString();

        this.hrId = new JSONObject(hrResponse).getString("id");
    }

    @Test
    void shouldCreateAndRetrieveVariousMeasures() throws Exception {
        String jwt = this.jwt;
        String hrId = this.hrId;

        String[] types = {"WEIGHT", "BPM", "RESPIRATORY_RATE", "TEMPERATURE"};
        double[] values = {12.0, 60.0, 60.0, 37.0};

        for (int i = 0; i < types.length; i++) {
            mockMvc.perform(post("/measures")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwt)
                            .content("""
                            {
                              "value": %f,
                              "measureType": "%s",
                              "healthRecordId": %s,
                              "creationDate": "2026-03-02"
                            }
                            """.formatted(values[i], types[i], hrId)))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/measures/" + hrId + "/weight").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").value(12.0));
    }

    @Test
    void shouldModifyExistingMeasure() throws Exception {
        String jwt = this.jwt;
        String hrId = this.hrId;

        String measureRes = mockMvc.perform(post("/measures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                        { "value": 10, "measureType": "WEIGHT", "healthRecordId": %s, "creationDate": "2026-03-02" }
                        """.formatted(hrId)))
                .andReturn().getResponse().getContentAsString();

        String measureId = new JSONObject(measureRes).getString("id");

        mockMvc.perform(put("/measures/" + hrId + "/" + measureId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("15"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/measures/" + hrId + "/weight").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").value(15.0));
    }

    @Test
    void shouldDeleteMeasure() throws Exception {
        String jwt = this.jwt;
        String hrId = this.hrId;

        String measureRes = mockMvc.perform(post("/measures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                        { "value": 20, "measureType": "BPM", "healthRecordId": %s, "creationDate": "2026-03-02" }
                        """.formatted(hrId)))
                .andReturn().getResponse().getContentAsString();

        String measureId = new JSONObject(measureRes).getString("id");

        mockMvc.perform(delete("/measures/" + hrId + "/" + measureId)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/measures/" + hrId + "/bpm").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
